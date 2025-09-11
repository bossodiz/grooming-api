pipeline {
  agent any
  options { timestamps() }

  environment {
    REGISTRY = "docker.io"
    DOCKERHUB_CREDENTIALS = "dockerhub-bossodiz"
    IMAGE_NAME = "bossodiz/grooming-api"
    STACK_DIR = "/apps/stack"
  }

  stages {
    stage('Checkout') { steps { checkout scm } }

    stage('Init Vars') {
      steps {
        script {
          env.BRANCH_NAME = 'main' // ถ้า job นี้ build แค่ main ก็ฟิกไปเลย
          env.SHORT_SHA   = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
          env.BRANCH_SAFE = env.BRANCH_NAME
          env.TAG         = "${env.BRANCH_SAFE}-${env.SHORT_SHA}"
          echo "BRANCH_NAME=${env.BRANCH_NAME}, TAG=${env.TAG}"
        }
      }
    }

    stage('Docker Build') {
      steps {
        sh """
          set -e
          docker build -t ${IMAGE_NAME}:${TAG} .
          if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ]; then
            docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest
          fi
        """
      }
    }

    stage('Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh """
            set -e
            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin ${REGISTRY}
            docker push ${IMAGE_NAME}:${TAG}
            if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ]; then
              docker push ${IMAGE_NAME}:latest
            fi
            docker logout ${REGISTRY}
          """
        }
      }
    }

    stage('Deploy (docker compose up)') {
      when { anyOf { branch 'main'; branch 'master' } }
      steps {
        sh """
          set -e
          mkdir -p ${STACK_DIR}
          touch ${STACK_DIR}/.env

          # update API_TAG
          if grep -q '^API_TAG=' ${STACK_DIR}/.env; then
            sed -i 's/^API_TAG=.*/API_TAG=${TAG}/' ${STACK_DIR}/.env
          else
            echo "API_TAG=${TAG}" >> ${STACK_DIR}/.env
          fi

          # ensure defaults
          grep -q '^WEB_TAG=' ${STACK_DIR}/.env       || echo "WEB_TAG=latest" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_ROOT_PASSWORD=' ${STACK_DIR}/.env || echo "MYSQL_ROOT_PASSWORD=1234" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_DATABASE=' ${STACK_DIR}/.env || echo "MYSQL_DATABASE=prod" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_USER=' ${STACK_DIR}/.env     || echo "MYSQL_USER=admin" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_PASSWORD=' ${STACK_DIR}/.env || echo "MYSQL_PASSWORD=1234" >> ${STACK_DIR}/.env
          grep -q '^JWT_SECRET=' ${STACK_DIR}/.env     || echo "JWT_SECRET=f0df5494b0c8279ef9b0da4c26a3714e" >> ${STACK_DIR}/.env

          cd ${STACK_DIR}
          docker compose pull api || true
          docker compose up -d api
        """
      }
    }
  }

  post { always { script { deleteDir() } } }
}

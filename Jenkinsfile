pipeline {
  agent any

  environment {
    REGISTRY = "docker.io"
    DOCKERHUB_CREDENTIALS = "dockerhub-bossodiz"
    IMAGE_NAME = "bossodiz/grooming-api"
    // tag = <branch>-<shortSHA>
    SHORT_SHA = sh(returnStdout: true, script: "git rev-parse --short HEAD").trim()
    BRANCH_SAFE = env.BRANCH_NAME.replaceAll('[^A-Za-z0-9._-]','-')
    TAG = "${BRANCH_SAFE}-${SHORT_SHA}"
    // deploy stack location (mounted from host C:\apps:/apps)
    STACK_DIR = "/apps/stack"
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Unit Test') {
      steps {
        sh """
          docker run --rm -v "\$PWD":/app -w /app maven:3.9-eclipse-temurin-21 \
            mvn -B -e -DskipTests=false test package
        """
      }
    }

    stage('Build Docker Image') {
      steps {
        sh """
          docker build -t ${IMAGE_NAME}:${TAG} .
          # สำหรับ main/master ติด latest ด้วย
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
      when { anyOf { branch 'main'; branch 'master'; } }
      steps {
        sh """
          # เขียน/อัปเดตไฟล์ .env สำหรับ stack
          mkdir -p ${STACK_DIR}
          touch ${STACK_DIR}/.env

          # อัปเดต API_TAG ให้เป็น tag ล่าสุด
          grep -q '^API_TAG=' ${STACK_DIR}/.env && \
            sed -i 's/^API_TAG=.*/API_TAG=${TAG}/' ${STACK_DIR}/.env || \
            echo "API_TAG=${TAG}" >> ${STACK_DIR}/.env

          # Deploy
          cd ${STACK_DIR}
          docker compose pull api || true
          docker compose up -d api
        """
      }
    }
  }

  post {
    always { cleanWs() }
  }
}

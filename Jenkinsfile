pipeline {
  agent any

  environment {
    REGISTRY = "docker.io"
    DOCKERHUB_CREDENTIALS = "dockerhub-bossodiz"
    IMAGE_NAME = "bossodiz/grooming-api"
    STACK_DIR = "/apps/stack"   // ถูก mount จากโฮสต์ (เช่น C:\apps:/apps)
  }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Init Vars') {
      steps {
        script {
          env.BRANCH_NAME = 'main'
          env.SHORT_SHA   = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
          env.BRANCH_SAFE = env.BRANCH_NAME.replaceAll(/[^A-Za-z0-9._-]/, '-')
          env.TAG         = "${env.BRANCH_SAFE}-${env.SHORT_SHA}"

          echo "BRANCH_NAME=${env.BRANCH_NAME}, TAG=${env.TAG}"
        }
      }
    }

    stage('Build & Test (Maven)') {
      steps {
        sh """
          set -e
          # หา pom.xml ตัวแรกใน repo (ลึกไม่เกิน 3 ชั้น); ถ้าเจอหลายตัวจะหยิบตัวแรก
          POM_PATH=\$(find . -maxdepth 3 -type f -name pom.xml | head -n1)
          if [ -z "\$POM_PATH" ]; then
            echo "❌ ไม่พบ pom.xml ใน repo"; exit 1
          fi
          echo "✅ ใช้ POM: \$POM_PATH"

          # รันใน container Maven โดย mount workspace (เผื่อ path มีช่องว่าง)
          docker run --rm \
            -v \"${WORKSPACE}\":/app \
            -w /app \
            -e MAVEN_CONFIG=/root/.m2 \
            maven:3.9-eclipse-temurin-21 \
            mvn -B -e -DskipTests=false -f "\$POM_PATH" test package
        """
      }
    }


    stage('Build Docker Image') {
      steps {
        sh '''
          docker build -t ${IMAGE_NAME}:${TAG} .
          if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ]; then
            docker tag ${IMAGE_NAME}:${TAG} ${IMAGE_NAME}:latest
          fi
        '''
      }
    }

    stage('Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh '''
            echo "${DOCKER_PASS}" | docker login -u "${DOCKER_USER}" --password-stdin ${REGISTRY}
            docker push ${IMAGE_NAME}:${TAG}
            if [ "${BRANCH_NAME}" = "main" ] || [ "${BRANCH_NAME}" = "master" ]; then
              docker push ${IMAGE_NAME}:latest
            fi
            docker logout ${REGISTRY}
          '''
        }
      }
    }

    stage('Deploy (docker compose up)') {
      when { anyOf { branch 'main'; branch 'master' } }
      steps {
        sh '''
          mkdir -p ${STACK_DIR}
          touch ${STACK_DIR}/.env

          # update API_TAG
          if grep -q '^API_TAG=' ${STACK_DIR}/.env; then
            sed -i 's/^API_TAG=.*/API_TAG='${TAG}'/' ${STACK_DIR}/.env
          else
            echo "API_TAG=${TAG}" >> ${STACK_DIR}/.env
          fi

          # ensure defaults
          grep -q '^WEB_TAG=' ${STACK_DIR}/.env       || echo "WEB_TAG=latest" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_ROOT_PASSWORD=' ${STACK_DIR}/.env || echo "MYSQL_ROOT_PASSWORD=change-me" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_DATABASE=' ${STACK_DIR}/.env || echo "MYSQL_DATABASE=grooming" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_USER=' ${STACK_DIR}/.env     || echo "MYSQL_USER=grooming" >> ${STACK_DIR}/.env
          grep -q '^MYSQL_PASSWORD=' ${STACK_DIR}/.env || echo "MYSQL_PASSWORD=change-me" >> ${STACK_DIR}/.env
          grep -q '^JWT_SECRET=' ${STACK_DIR}/.env     || echo "JWT_SECRET=please-change-me" >> ${STACK_DIR}/.env

          cd ${STACK_DIR}
          docker compose pull api || true
          docker compose up -d api
        '''
      }
    }
  }

  post {
    always {
      script { deleteDir() } // แทน cleanWs เพื่อลดปัญหา context
    }
  }
}

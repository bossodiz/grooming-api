pipeline {
  agent any
  options { ansiColor('xterm'); timestamps() }

  parameters {
    string(name: 'BRANCH', defaultValue: 'main', description: 'Git branch to deploy')
  }

  environment {
    PROJECT  = "bbp_api_${params.BRANCH}"
    WORKDIR  = "workspace-api-${params.BRANCH}"
  }

  stages {
    stage('Checkout') {
      steps {
        deleteDir()
        checkout([
          $class: 'GitSCM',
          branches: [[name: "*/${params.BRANCH}"]],
          userRemoteConfigs: [[
            url: 'https://github.com/bossodiz/grooming-api.git',
            credentialsId: 'github-pat'   // ใช้ credential ที่คุณสร้างไว้
          ]]
        ])
      }
    }

    stage('Build API') {
      steps {
        sh """
          docker network create appnet || true
          docker compose build api
        """
      }
    }

    stage('Deploy API') {
      steps {
        sh """
          docker compose up -d db
          docker compose up -d --no-deps api
          docker compose ps
        """
      }
    }
  }

  post {
    success { echo "✅ API deployed on project ${PROJECT}" }
    failure { echo "❌ API deploy failed for branch ${params.BRANCH}" }
  }
}

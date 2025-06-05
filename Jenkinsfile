pipeline {
    agent any

    environment {
        // ใช้ Jenkins Credentials ID ที่เราสร้างไว้
        GIT_CREDENTIALS_ID = 'github-pat'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/youruser/your-java-project.git',
                    credentialsId: "${GIT_CREDENTIALS_ID}"
            }
        }

        stage('Build') {
            steps {
                echo '🔧 Building project...'
                sh './mvnw clean package -DskipTests' // หรือ mvn ถ้าใช้ Maven ที่ติดตั้งไว้
            }
        }

        stage('Test') {
            steps {
                echo '✅ Running unit tests...'
                sh './mvnw test'
            }
        }

        stage('Deploy') {
            steps {
                echo '🚀 Deploying...'
                // ตัวอย่าง: Copy JAR ไป server (ถ้าติดตั้ง SCP), หรือ Deploy Docker ก็ได้
                // SCP แบบง่าย (ต้องติดตั้ง ssh-agent plugin + ใส่ credentials SSH ไว้)
                // sh 'scp -i /path/to/key.pem target/your-app.jar ubuntu@your-server:/home/ubuntu/'

                // หรือ Deploy ผ่าน Docker
                // sh 'docker build -t your-image-name .'
                // sh 'docker run -d -p 8080:8080 your-image-name'
            }
        }
    }

    post {
        success {
            echo '🎉 Deployment succeeded!'
        }
        failure {
            echo '❌ Deployment failed!'
        }
    }
}

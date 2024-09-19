pipeline {
    agent any
    environment {
        DOCKER_REGISTRY = "mina2110"
        IMAGE_NAME = "myapp-todolist"
        TAG = "latest"
    }
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/AbdulrhmanOraby1/todoapp.git'
            }
        }
        stage('Install Nixpacks') {
            steps {
                sh 'npm install -g @railway/nixpacks'
            }
        }
        stage('Build Docker Image with Nixpacks') {
            steps {
                sh 'nixpacks build .'
            }
        }
        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'abf407cb-1431-4210-a068-aef4a58a38d8', usernameVariable: 'mina2110', passwordVariable: 'Anaminayounan1')]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    sh "docker tag nixpacks-app-image ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}"
                    sh "docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${TAG}"
                }
            }
        }
       
    }
}
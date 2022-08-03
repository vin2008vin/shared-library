import java.text.SimpleDateFormat;
import java.util.Date;

def call(String dockerImage, Map config=[:], Closure body={}) {
  pipeline {
    agent any

    environment {
        AWS_ACCOUNT_ID="647834768285"
        AWS_DEFAULT_REGION="ap-south-1"
        IMAGE_REPO_NAME="sample"
        IMAGE_TAG="0.1.0"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
    }

    options {
      disableConcurrentBuilds()
      buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
      timeout(time: 60, unit: "MINUTES")
    }

    stages {
         stage('Logging into AWS ECR') {
            steps {
                script {
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }
                 
            }
        }
        
        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'b726746f-abea-40cf-b1a6-318d9e212b80', url: 'https://github.com/vin2008vin/shared-library.git']]])     
            }
        }
  
    // Building Docker images
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build "${IMAGE_REPO_NAME}:${IMAGE_TAG}"
        }
      }
    }
   
    // Uploading Docker images into AWS ECR
    stage('Pushing to ECR') {
     steps{  
         script {
                sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG"
                sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
         }
        }
      }
    }
    // TODO: maybe only email out when we are building master or at least not PRs
    post {
      failure {
        emailext(
          attachLog: true,
          recipientProviders: [developers()],
          body: "Build failed (see ${env.BUILD_URL})",
          subject: "[JENKINS] ${env.JOB_NAME} failed",
        )
      }
    }
  }
}

import java.text.SimpleDateFormat;
import java.util.Date;

def call(Map config=[:]) {
  pipeline {
    agent any
    environment {
        def AWS_ACCOUNT_ID = 647834768285
        def AWS_DEFAULT_REGION = ap-south-1
        def IMAGE_REPO_NAME = sample
        def IMAGE_TAG = 0.1.0
        def REPOSITORY_URI = ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}
    }
	
	def call(Map config) {
		sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
		}
    
        
    def call(Map config=[:]) {
    checkout scm
    sh "checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'b726746f-abea-40cf-b1a6-318d9e212b80', url: 'https://github.com/vin2008vin/jenkins-pipeline.git']]])"
    }
  
    // Building Docker images
	def call(Map config) {
		sh "dockerImage = docker.build ${IMAGE_REPO_NAME}:${IMAGE_TAG}"
	}
   
    // Uploading Docker images into AWS ECR
	def call(Map config) {
	            sh """
				docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${REPOSITORY_URI}:$IMAGE_TAG
                docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}
				"""
			}
    }
}

pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID="647834768285"
        AWS_DEFAULT_REGION="ap-south-1"
        IMAGE_REPO_NAME="sample"
        IMAGE_TAG="0.1.0"
        REPOSITORY_URI = "647834768285.dkr.ecr.ap-south-1.amazonaws.com/sample"
    }
   
    stages {
        
         stage('Logging into AWS ECR') {
            steps {
                script {
                sh "aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 647834768285.dkr.ecr.ap-south-1.amazonaws.com"
                }
                 
            }
        }
        
        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: 'b726746f-abea-40cf-b1a6-318d9e212b80', url: 'https://github.com/vin2008vin/jenkins-pipeline.git']]])     
            }
        }
  
    // Building Docker images
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build "sample:0.1.0"
        }
      }
    }
   
    // Uploading Docker images into AWS ECR
    stage('Pushing to ECR') {
     steps{  
         script {
                sh "docker tag sample:0.1.0 647834768285.dkr.ecr.ap-south-1.amazonaws.com/sample:0.1.0"
                sh "docker push 647834768285.dkr.ecr.ap-south-1.amazonaws.com/sample:0.1.0"
         }
        }
      }
    }
}

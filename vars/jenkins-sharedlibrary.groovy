import com.mycompany.colinbut.*
import com.mycompany.colinbut.Git*
import jenkins.model.*
jenkins = Jenkins.instance

def call(Map args=[:]) {
    node {
        stage('Checkout') {
            new Git(this).checkout(args.repo)
        }
    }
    return this
}

def compile(Map args) {
    node {
        stage('Logging into AWS ECR') {
            sh "aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 647834768285.dkr.ecr.ap-south-1.amazonaws.com"
        }
    }
    return this
}

def buildDockerImage(Map args) {
    node {
        def dockerEcr = new DockerEcr(this)
        stage('Build Docker Image') {
            docker.build sample:0.1.0
        }
    }
    return this
}

def publishDockerImage(Map args) {
    node {
        stage('Publish Docker Image') {
            sh "docker tag sample:0.1.0 647834768285.dkr.ecr.ap-south-1.amazonaws.com/sample:0.1.0"
            sh "docker push 647834768285.dkr.ecr.ap-south-1.amazonaws.com/sample:0.1.0"
        }
    }
    return this
}

def additionalPostBuildSteps(Closure body= { }) {
    node {
        body()
    }
}

import com.mycompany.colinbut.*
import com.mycompany.colinbut.Git*
import jenkins.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import com.buildah.federated.buildcontainers.stages.*
import groovy.*


def call(Map args=[:]) {
    node {
        stage('Checkout') {
            new Git(this).checkout(args.repo)
        }
    }
    return this
}

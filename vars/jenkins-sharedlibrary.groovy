import com.mycompany.colinbut.*
import com.mycompany.colinbut.Git*
import jenkins.model.*
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import com.buildah.federated.buildcontainers.stages.*
package groovy.lang


def call(Map args=[:]) {
    node {
        stage('Checkout') {
            new Git(this).checkout(args.repo)
        }
    }
    return this
}

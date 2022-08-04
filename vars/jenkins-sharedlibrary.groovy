import com.mycompany.colinbut.*
import com.mycompany.colinbut.Git*

def call(body) {
    node {
        stage('Checkout') {
            new Git(this).checkout(args.repo)
        }
    }
    return this
}

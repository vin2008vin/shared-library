package com.mycompany.colinbut

class Git implements Serializable {

    private final def script

    Git(def script) {
        this.script = script
    }

    def checkout(String repo) {
        this.script.git credentialsId: Constants.JENKINS_GITHUB_CREDENTIALS_ID, url: "https://github.com/vin2008vin/shared-library.git"
    }

    String commitHash() {
        return this.script.sh(script: getLatestGitCommitHashCommand(), returnStdout: true).trim()
    }

    private static String getLatestGitCommitHashCommand() {
        return "git rev-parse HEAD"
    }
}

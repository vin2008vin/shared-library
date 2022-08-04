package com.mycompany.colinbut

class Git implements Serializable {

    private final def script

    Git(def script) {
        this.script = script
    }

    def checkout(String repo) {
        this.script.git, url: "https://github.com/vin2008vin/${repo}.git"
    }

    String commitHash() {
        return this.script.sh(script: getLatestGitCommitHashCommand(), returnStdout: true).trim()
    }

    private static String getLatestGitCommitHashCommand() {
        return "git rev-parse HEAD"
    }
}

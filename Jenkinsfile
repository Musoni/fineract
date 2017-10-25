def buildDevelopBranch() {
    echo "Develop branch"
}

def buildReleaseBranch() {
    echo "Release branch"
}

def buildMasterBranch() {
    echo "Master branch"
}

node {
    checkout scm
    
    def name = env.BRANCH_NAME
    } else if (name == 'develop') {
        buildDevelopBranch()
    } else if (name.startsWith('release/')) {
        buildReleaseBranch()
    } else if (name == 'master') {
        buildMasterBranch()
    } else {
        error "Don't know what to do with this branch: ${name}"
    }
}

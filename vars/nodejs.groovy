// @Library('roboshop@main') _

def lintchecks() {
    sh "echo Installing JSLint"
    sh "npm install jslint"
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo Lint checks completed"
}

def sonarcheck() {
    sh "env"
    sh "sonar-scanner -Dsonar.host.url=http://${env.SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${env.COMPONENT} -Dsonar.login=e66657d47a6ff847cd4ff38fbc26b74a4a9d359f"
    sh "bash qualitygate.sh || true"             
}

pipeline {
    agent any 
    environment {
        SONAR_URL = "172.31.89.159"
        SONAR_CRED = credentials('SONAR_CRED')
        COMPONENT = "${COMPONENT}" // Define your component name here or pass it from outside
    }
    stages {
        stage('Lint Checks') {
            steps {
                script {
                    lintchecks()
                }
            }
        }
        stage('Sonar Check') {
            steps {
                script {
                    sonarcheck()
                }
            }
        }
        stage('Generating Artifacts') {
            steps {
                sh "echo Generating Artifacts"
                sh "npm install"
            }
        }
    }
}

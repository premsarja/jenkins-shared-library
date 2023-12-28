// @Library('roboshop@main') _

def lintchecks() {
    sh "echo Installing JSLint"
    sh "npm install jslint"
    sh "echo Starting lint checks********** ${env.COMPONENT}"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo Lint checks completed"
}

def sonarcheck() {
    sh "env"
    sh "sonar-scanner -Dsonar.host.url=http://172.31.89.159:9000/ -Dsonar.sources=. -Dsonar.projectKey=${env.COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
    sh "bash qualitygate.sh || true"             
}

pipeline {
    agent any 
    environment {
        SONAR_URL = "172.31.89.159"
        SONAR_CRED = credentials('SONAR_CRED')
        // COMPONENT = "${env.COMPONENT}" // Replace with your component name or pass it externally
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
                // Add any other necessary commands for generating artifacts
            }
        }
    }
}

// @Library('roboshop@main') _

def lintchecks() {
    sh "echo Installing JSLint"
    sh "npm install jslint"
    sh "echo Starting lint checks********** ${env.COMPONENT}"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo Lint checks completed for ${env.COMPONENT}"
}

def sonarcheck() {
    sh "env"
    sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=83b03e3e897fb76ef13f25c264b75e81ed043ed"
    sh "bash qualitygate.sh || true"
}

def call() {
    pipeline {
        agent any 
        environment {
            SONAR_URL = "172.31.89.159"
            SONAR_CRED = credentials('SONAR_CRED')
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
}

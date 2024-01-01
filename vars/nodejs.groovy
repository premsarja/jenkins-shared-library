// @Library('roboshop@main') _

def call() {
    pipeline {
        agent any 
        environment {
            SONAR_URL = "172.31.89.159"
            // SONAR_CRED = credentials('SONAR_CRED')
        }    
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintcommon.lintchecks()
                    }
                }
            }
            stage('Sonar Check') {
                steps {
                    script {
                        common.sonarcheck()
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

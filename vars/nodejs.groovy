// @Library('roboshop@main') _

def call() {
    pipeline {
        agent any 
        environment {
            SONAR_URL = "172.31.89.159"
            NEXUS_URL = "172.31.60.99"
            NEXUS_CRED =credentials('NEXUS_CRED')
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
                        env.ARGS="-Dsonar.java.binaries=target/"
                        common.sonarcheck()
                    }
                }
            }
            stage('Test Cases') {
                parallel {
                    stage('Unit Testing') {
                        steps {
                            sh "echo Starting Unit Testing"
                            sh "echo Unit Testing Completed"
                        }
                    }
                    stage('Integration Testing') {
                        steps {
                            sh "echo Starting Integration Testing"
                            sh "echo Integration Testing Completed"
                        }
                    }
                    stage('Functional Testing') {
                        steps {
                            sh "echo Starting Functional Testing"
                            sh "echo Functional Testing Completed"
                        }
                    }
                }
            }
            stage('Generating Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                    // Add any other necessary commands for generating artifacts
                }
            }
            stage('uploading  Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    sh "echo uploading Artifacts"
                    sh curl -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip                }
            }
        }
    }
}

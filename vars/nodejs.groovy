def call() {
    pipeline {
        agent any
        environment {
            SONAR_URL = "172.31.89.159"
            NEXUS_URL = "172.31.60.99"
            SONAR_CRED  = credentials('SONAR_CRED')
            // NEXUS_CRED  = credentials('NEXUS_CRED')
        }
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
            stage('Sonar Checks') {
                steps {
                    script {
                            env.ARGS="-Dsonar.sources=."
                            common.sonarChecks()
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
            stage('Check The Release') {
                when {
                    expression { env.TAG_NAME != null }
                }
                steps {
                    script {
                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
                        print UPLOAD_STATUS
                    }
                }
            }
            stage('Generating Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
                }
                steps {
                    sh "echo Generating Artifiacts...."
                    sh "npm install"
                    sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                    sh "ls -ltr"
                    }
                }
            stage('Uploading Artifacts') {
                when {
                    expression { env.TAG_NAME != null }
                    expression { env.UPLOAD_STATUS == "" }
                }
                steps {
                    sh '''
                        echo Uploading ${COMPONENT} artifact to nexus
                        curl -v -u ${NEXUS_CRED_USR}:${NEXUS_CRED_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
                        echo Uploading ${COMPONENT} artifact to nexus is completed
                    ''' 
                    }
                }
            }
        }
    }
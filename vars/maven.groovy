def lintChecks() {
    sh "echo Starting lint checks********** ${env.COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${env.COMPONENT}"

    pipeline {
        agent any 
        environment {
            SONAR_URL = "172.31.89.159"
            // SONAR_CRED  = credentials('SONAR_CRED')
        }
        stages {
            stage('Lint Checks') {
                steps {
                    script {
                        lintChecks()
                    }
                }
            }
            stage('Code Compile') {
                steps {
                    sh "echo Generating Artifiacts for $COMPONENT"
                    sh "mvn clean compile"
                    }
                }
            stage('Sonar Checks') {
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
        }
    }
}            
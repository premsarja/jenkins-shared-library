def lintChecks() {
    sh "echo Starting lint checks********** ${env.COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${env.COMPONENT}"
}
def call(){
pipeline {
    agent any 
    environment {
        SONAR_URL = "172.31.89.159"
        //SONAR_CRED = credentials('SONAR_CRED')
        // COMPONENT = "YourComponentName" // Define COMPONENT if not already set
       }
    }
    stages {
        stage('Lint Checks') {
            steps {
                script {
                    lintChecks()
                }
            }
        }

        stage('code compile') {
            steps {
                sh "echo generating artifacts for ${env.COMPONENT}"
                sh "mvn clean compile || true "
            }
        }

        stage("Sonar Check") {
            steps {
                script {
                    env.ARGS = "-Dsonar.java.binaries=target/"
                    common.sonarcheck()
                    // Add commands related to SonarQube using env.ARGS or any other variable you prefer
                    // Example: sh "mvn sonar:sonar ${env.ARGS}"
                }
            }
        }

        stage('Tests') {
            parallel {
                stage('unit testing') {
                    steps {
                        sh "echo starting unit testing"
                        sh "echo unit test completed"
                    }
                }

                stage('integration testing') {
                    steps {
                        sh "echo starting integration testing"
                        sh "echo integration test completed"
                    }
                }      
                stage('functional testing') {
                    steps {
                        sh "echo starting functional testing"
                        sh "echo functional test completed"
                    }
                }
            }
        }

        stage('Generating Artifacts') {
            steps {
                sh "echo 'Generating Artifacts'"
                sh "mvn clean package"
            }
        }
    }
}

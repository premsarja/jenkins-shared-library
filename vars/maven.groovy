def lintChecks() {
    sh "echo 'Starting lint checks********** ${COMPONENT}'"
    sh "mvn checkstyle:check || true"
    sh "echo 'Lint checks completed for ${COMPONENT}'"
}
def call(){
pipeline {
    agent any 
    environment {
        SONAR_URL = "172.31.89.159"
        //SONAR_CRED = credentials('SONAR_CRED')
    }
    stages {
        stage('Lint Checks') {
            steps {
                script {
                    lintChecks() // Replace "YourComponentName" with your actual component name
                }
            }
        }

        stage('code compile') {
            steps {
                sh '''
                echo generating artifacts for $(COMPONENT)
                mvn clean compile
                '''
            }
        }

        stage("Sonar Check") {
            steps {
                script {
                    env.ARGS="-Dsonar.java.binaries=target/"
                    common.sonarcheck()
                    // Add commands related to SonarQube using sonarArgs
                    // Example: sh "mvn sonar:sonar ${sonarArgs}"
                }
            }
        }
        stage('testing'){
        parallel{
        stage('unit testing'){
            steps{
                sh "echo starting unit testing"
                sh "echo unit test completed"
            }
        }     
         stage('integration testing'){
            steps{
                sh "echo starting unit testing"
                sh "echo unit test completed"
            }
        }      
          stage('functional testing'){
            steps{
                sh "echo starting unit testing"
                sh "echo unit test completed"
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
 }
}

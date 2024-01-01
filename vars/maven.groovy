def lintchecks() {
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${COMPONENT}"
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
                    lintchecks()
                }
            }
        }

        stage("Sonar Check") {
            steps {
                script{
                    ARGS="-Dsonar.java.binaries=target/" 
                    common.lintchecks()
                }
        
            }
        }


        stage('Generating Artifacts') {
            steps {
                sh "echo Generating Artifacts"
                sh "mvn clean package"
            }
        }

    }
  }
} 

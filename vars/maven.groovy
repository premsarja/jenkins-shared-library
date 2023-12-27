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

        stage('Generating Artifacts') {
            steps {
                sh "echo Generating Artifacts"
                sh "mvn clean package"
            }
        }

        stage("Sonar Check") {
            steps {
                sh "env"
                sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.java.binaries=target/ -Dsonar.projectKey=${COMPONENT} -Dsonar.login=e66657d47a6ff847cd4ff38fbc26b74a4a9d359f"
                sh "bash qualitygate.sh || true"             
            }
        }
      }
   }
}

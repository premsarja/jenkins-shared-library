def lintchecks() {
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${COMPONENT}"

    } 
  
    def call() {
    pipeline {
        agent any 
           stages {
            stage('Lint Checks') {
                steps {                
                    script{
                        lintchecks()
                    }
                }
            }
                steps{
                    sh "env"
                    sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=e66657d47a6ff847cd4ff38fbc26b74a4a9d359f"
                    sh " bash qualitygate.sh || true"  
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
    // @Library('roboshop@main') _
def lintchecks() {
    sh "echo Installing JSLint"
    sh "npm install jslint"
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "node_modules/jslint/bin/jslint.js server.js || true"
    sh "echo Lint checks completed"

    } 
  
    def call() {
    pipeline {
        agent any 
        environment{
            SONAR_URL="172.31.89.159"
            SONAR_CRED=credentials('SONAR_CRED')
        }
           stages {
            stage('Lint Checks') {
                steps {                
                    script{
                        lintchecks()
                    }
                }
            }

            stage('sonar check'){
                steps{
                    sh "env"
                    sh sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED_USR} -Dsonar-password=${SONAR_CRED}             
                }
            }
            stage('Generating Artifacts') {
                steps {
                    sh "echo Generating Artifacts"
                    sh "npm install"
                }
            }
        }
    
    }
}   
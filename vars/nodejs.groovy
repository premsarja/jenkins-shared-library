    // @Library('roboshop@main') _
 // this is for nodejs lint checks   
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
        stages {
         stage('Lint Checks') {
             steps {                
                     lintchecks()
             }
         }
         stage('sonar check'){
             environment{
                 SONAR_URL="172.31.89.159"
                 SONAR_CRED=credentials('SONAR_CRED')
             }
             steps{
                script{
                    sonarcheck()
                }
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
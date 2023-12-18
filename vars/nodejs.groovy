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
           stages {
            stage('Lint Checks') {
                steps {                
                    script{
                        lintchecks()
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
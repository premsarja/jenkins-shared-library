    // @Library('roboshop@main') _
    def call() {
    pipeline {
        agent any 
           stages {
            stage('Lint Checks') {
                steps {                
                        sh "echo Installing JSLint"
                        sh "npm install jslint"
                        sh "echo Starting lint checks**********"
                        sh "node_modules/jslint/bin/jslint.js server.js || true"
                        sh "echo Lint checks completed"
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
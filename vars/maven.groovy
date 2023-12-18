def lintchecks() {
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${COMPONENT}"

    } 
  
    def call() {
    pipeline {
        agent {
            label "shipping"
        }
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
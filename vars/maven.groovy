def lintChecks() {
    sh "echo Starting lint checks********** ${env.COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${env.COMPONENT}"
}

def call(){
    pipeline{
        agent any
        environment(
            SONAR_URL = "172.31.89.159"
        )
        
        stages{
            stage('lint checks'){
                steps{
                    script{
                        linkchecks()
                    }
                }
            }
        }

    }
}
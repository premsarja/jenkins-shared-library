def lintchecks() {
    sh "echo starting linkcjecks for ${COMPONENT}"
    sh "pylint *.py || true"
    sh " echo linkchecks completed for ${COMPONENT}"
}

def call() {
    pipeline{
        agent any
        stages{
            stage("lint charges"){
                steps{
                    script{
                        lintchecks()
                    }
                }
            }
                environment{
                        SONAR_URL="172.31.89.159"
                        SONAR_CRED=credentials('SONAR_CRED')
                }
                steps{
                    sh "env"
                    sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=e66657d47a6ff847cd4ff38fbc26b74a4a9d359f"
                    sh " bash qualitygate.sh || true"  
                    sh "echo $?"           
                }
            }
                

        }
    }
}
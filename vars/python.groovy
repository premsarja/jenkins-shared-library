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
            stage("sonar-checks"){
                environment{
                        SONAR_URL="172.31.89.159"
                        // SONAR_CRED=credentials('SONAR_CRED')
                }
                steps{
                    sh "env"
                    sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
                }
            }
                

        }
    }
}
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
                }
            stage('Sonar Check') {
                steps {
                    script {
                        env.ARGS="-Dsonar.java.binaries=target/"
                        common.sonarcheck()
                    }
                }
            }
                

        }

    }
}
def lintchecks() {
    sh "echo Starting lint checks********** ${COMPONENT}"
    sh "mvn checkstyle:check || true"
    sh "echo Lint checks completed for ${COMPONENT}"
}

pipeline {
    agent any 
    stages {
        stage('Lint Checks') {
            steps {                
                script {
                    lintchecks()
                }
            }
        }
        
        stage("Sonar Check") {
            environment {
                SONAR_URL="172.31.89.159"
                SONAR_CRED=credentials('SONAR_CRED')
            }
            steps {
                sh "env"
                sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=${SONAR_CRED}"
                sh "bash qualitygate.sh || true"             
            }
        }
        
        stage('Generating Artifacts') {
            steps {
                sh "echo Generating Artifacts"
                sh "mvn clean package"
            }
        }
    }
}

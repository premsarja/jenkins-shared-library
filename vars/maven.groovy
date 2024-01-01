def lintChecks(component) {
    sh "echo 'Starting lint checks********** ${component}'"
    sh "mvn checkstyle:check || true"
    sh "echo 'Lint checks completed for ${component}'"
}

pipeline {
    agent any 
    environment {
        SONAR_URL = "172.31.89.159"
        //SONAR_CRED = credentials('SONAR_CRED')
    }
    stages {
        stage('Lint Checks') {
            steps {
                script {
                    lintChecks("YourComponentName")
                }
            }
        }

        stage("Sonar Check") {
            steps {
                script {
                    def sonarArgs = "-Dsonar.java.binaries=target/"
                    // Add commands related to SonarQube using sonarArgs
                    // Example: sh "mvn sonar:sonar ${sonarArgs}"
                }
            }
        }

        stage('Generating Artifacts') {
            steps {
                sh "echo 'Generating Artifacts'"
                sh "mvn clean package"
            }
        }
    }
}

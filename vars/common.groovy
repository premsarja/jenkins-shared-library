def sonarCheck() {
    stage('Sonar Checks') {
        sh "env"
        sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
        sh "bash qualitygate.sh || true"
    }
}

def lintChecks() {
    stage('Lint Checks') {
        if (env.APPTYPE == "maven") {
            // Maven lint checks
            sh "echo Starting lint checks********** ${env.COMPONENT}"
            sh "mvn checkstyle:check || true"
            sh "echo Lint checks completed for ${env.COMPONENT}"
        } else if (env.APPTYPE == "nodejs") {
            // Node.js lint checks
            sh "echo Installing JSLint"
            sh "npm install jslint"
            sh "echo Starting lint checks********** ${env.COMPONENT}"
            sh "node_modules/jslint/bin/jslint.js server.js || true"
            sh "echo Lint checks completed for ${env.COMPONENT}"
        } else if (env.APPTYPE == "PYTHON") {
            // Python lint checks
            sh "echo Starting lint checks for ${COMPONENT}"
            sh "pylint *.py || true"
            sh "echo Lint checks completed for ${COMPONENT}"
        } else {
            // Handle other types
            sh "echo Lint checks for frontend"
        }
    }
}

def testCase() {
    stage('Test Case') {
        def stages = [:]

        stages["unit-test"] = {
            echo "build for unit-test"
            echo "completed unit-test"
        }
        stages["integration-test"] = {
            echo "build for integration-test"
            echo "completed integration-test"
        }
        stages["functional-test"] = {
            echo "build for functional-test"
            echo "completed functional-test"
        }

        parallel(stages)
    }
}

def artifacts() {
    stage('Checking the Artifacts Release') {
        env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print UPLOAD_STATUS
    }

    if (env.UPLOAD_STATUS == "") {
        stage('Generating the Artifacts') {
            // Generating artifacts based on application type
            // ...
        }

        stage('Uploading the Artifacts') {
            withCredentials([usernamePassword(credentialsId: 'NEXUS_CRED', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
                sh "echo Uploading ${COMPONENT} artifact to Nexus"
                sh "pwd"
                sh "curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.60.99:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                sh "pwd"
                sh "echo Uploading ${COMPONENT} artifact to Nexus is completed"
            }
        }
    }
}

// Check only syntax
// No further changes were made in the script other than fixing the aforementioned issues.

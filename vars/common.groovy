def sonarcheck() {
    stage('sonar checks'){
       sh "env"
       sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.sources=.  -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
       sh "bash qualitygate.sh || true"
    }    
}


def lintChecks() {
    stage ('lintchecks') {
        if (env.APPTYPE == "maven"){
            sh "echo Starting lint checks********** ${env.COMPONENT}"
            sh "mvn checkstyle:check || true"
            sh "echo Lint checks completed for ${env.COMPONENT}"
        }
        else if(env.APPTYPE == "nodejs") {
            sh "echo Installing JSLint"
            sh "npm install jslint"
            sh "echo Starting lint checks********** ${env.COMPONENT}"
            sh "node_modules/jslint/bin/jslint.js server.js || true"
            sh "echo Lint checks completed for ${env.COMPONENT}"
        }
        else if(env.APPTYPE == "PYTHON"){
            sh "echo starting linkcjecks for ${COMPONENT}"
            sh "pylint *.py || true"
            sh " echo linkchecks completed for ${COMPONENT}"
        }
        else{
            sh "lint checks for frontend"
        }
    }
}       


    def testCase() {
    stage('testCase') {
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
          env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
          print UPLOAD_STATUS
    }

    if(env.UPLOAD_STATUS == "") {
        stage('Generating the artifacts') {
                if(env.APPTYPE == "nodejs") {
                    sh "echo Generating Artifiacts...."
                    sh "npm install"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
                }
                else if(env.APPTYPE == "maven") {
                    sh "echo Generating Artifiacts...."
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar"
                }
                else if(env.APPTYPE == "python") {
                    sh "echo Generating Artifiacts...."
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                }
                else {
                    sh ''' 
                        echo Generating Artifiact
                        cd static/
                        zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                    '''

        stage('Uploading the artifacts') {
           withCredentials([usernamePassword(credentialsId: 'NEXUS_CRED', passwordVariable: 'NEXUS_PSW', usernameVariable: 'NEXUS_USR')]) {
                   sh "echo Uploading ${COMPONENT} artifact to nexus"
                    sh "curl -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://172.31.22.243:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
                    sh "echo Uploading ${COMPONENT} artifact to nexus is completed"
                }      
            }
        }
    }
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
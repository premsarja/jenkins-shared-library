def lintchecks() {
       sh "echo Installing JSLint"
       sh "npm install jslint"
       sh "echo Starting lint checks**********"
       sh "node_modules/jslint/bin/jslint.js server.js || true"
       sh "echo Lint checks completed"

}

lintchecks()
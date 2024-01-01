def sonarcheck() {
    sh "env"
    sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ $ARGS -Dsonar.sources=.  -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
    sh "bash qualitygate.sh || true"
}

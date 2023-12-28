def sonarcheck(){
steps{
  sh "env"
  sh "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=e66657d47a6ff847cd4ff38fbc26b74a4a9d359f"
  sh " bash qualitygate.sh || true"             
  }
}  

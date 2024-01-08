def call() {
    node {
        git branch: 'main', url: 'https://github.com/premsarja/payment.git'
        common.lintChecks()  
        env.ARGS="-Dsonar.sources=."
        env.SONAR_URL="172.31.89.159"
        env.NEXUS_URL="172.31.60.99"
        env.TAG_NAME= ""
        common.sonarCheck()
        common.testCase()
        common.artifacts()
    }
}


// declarative pipeline

// def call() {
//     pipeline{
//         agent any
//         environment{
//             SONAR_URL="172.31.89.159"
//             NEXUS_URL="172.31.60.99"   
//                         // SONAR_CRED=credentials('SONAR_CRED')
//         }
//         stages{
//             stage("lint charges"){
//                 steps{
//                     script{
//                         lintchecks()
//                     }
//                 }
//             }
//             stage("sonar-checks"){
//                 steps{
//                     sh "env"
//                     sh "sonar-scanner -X -Dsonar.host.url=http://${SONAR_URL}:9000/ -Dsonar.sources=. -Dsonar.projectKey=${COMPONENT} -Dsonar.login=admin -Dsonar.password=password"
//                 }
//             }
//             stage('Test Cases') {
//                 parallel {
//                     stage('Unit Testing') {
//                         steps {
//                             sh "echo Starting Unit Testing"
//                             sh "echo Unit Testing Completed"
//                         }
//                     }
//                     stage('Integration Testing') {
//                         steps {
//                             sh "echo Starting Integration Testing"
//                             sh "echo Integration Testing Completed"
//                         }
//                     }
//                     stage('Functional Testing') {
//                         steps {
//                             sh "echo Starting Functional Testing"
//                             sh "echo Functional Testing Completed"
//                         }
//                     }
//                 }
//             }
//             stage('Check The Release') {
//                 when {
//                     expression { env.TAG_NAME != null }
//                 }
//                 steps {
//                     script {
//                         env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT} | grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                         print UPLOAD_STATUS
//                     }
//                 }
//             }
//             stage('Generating Artifacts') {
//                 steps {
//                     sh "echo Artifact Generation Complete"
//                     }
//                 }
//             }
//         }
//     }
// @Library('roboshop@main') _

def call() {
    node {
        common.lintChecks()  
        env.ARGS="-Dsonar.sources=."
        env.SONAR_URL="172.31.89.159"
        env.NEXUS_URL="172.31.60.99"
        common.sonarcheck()
        common.testCase()
        common.artifact()
    }
}    



//  declarative pipeline

// def call() {
//     pipeline {
//         agent any 
//         environment {
//             SONAR_URL = "172.31.89.159"
//             NEXUS_URL = "172.31.60.99"
//             // NEXUS_CRED = credentials('NEXUS_CRED')
//             // SONAR_CRED = credentials('SONAR_CRED')
//         }    
//         stages {
//             stage('Lint Checks') {
//                 steps {
//                     script {
//                         lintcommon.lintchecks()
//                     }
//                 }
//             }
//             stage('Sonar Check') {
//                 steps {
//                     script {
//                         env.ARGS="-Dsonar.java.binaries=target/"
//                         common.sonarcheck()
//                     }
//                 }
//                }
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
//                     expression {  env.TAG_NAME != null }
//                 }
//                 steps {
//                     script {
//                         echo "TAG_NAME: ${env.TAG_NAME}" // Print TAG_NAME value for debugging
//                         env.UPLOAD_STATUS = sh(returnStdout: true, script: "curl -L -s http://${NEXUS_URL}:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
//                         echo "UPLOAD_STATUS: ${env.UPLOAD_STATUS}" // Print UPLOAD_STATUS for debugging
//                         println env.UPLOAD_STATUS // Print UPLOAD_STATUS content for debugging}
//                     }
//                 }
//             }
//             stage('Generating Artifacts') {
//                 when {
//                         expression { env.TAG_NAME != null }
//                         expression { env.UPLOAD_STATUS == '' }
//                     }
                      
//                 steps {
//                     sh "echo Generating Artifacts..."
//                     sh "npm install"
//                     sh "zip ${COMPONENT}-${TAG_NAME}.zip node_modules server.js"
//                     sh "ls -ltr"
//                     }
//                 }
//                  stage('Uploading Artifacts') {
//                      when {
//                          expression { env.TAG_NAME != null }
//                         expression { env.UPLOAD_STATUS == '' }

//                     }

//                   steps {
//                     sh '''
//                         echo Uploading ${COMPONENT} artifact to Nexus...
//                         curl -v admin:password --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
//                         echo Uploading ${COMPONENT} artifact to Nexus is completed
//                     ''' 
//                 }
//             }
//         }
//     }
// }

pipeline {
    agent any

    tools {
        jdk "JDK21"
        maven "MAVEN3.9"
    }
    stages {
        stage ("BUILD"){
             steps {
                 sh 'mvn clean install -DskipTests'
             }
              post {
                 success {
                     echo 'Now Archiving...'
                     archiveArtifacts artifacts: '**/target/*.war'
                 }
             }
        }
        stage ("TEST"){
            steps {
                 sh 'mvn test'
            }
        }
        stage("INTEGRATION TEST"){
            steps {
                sh 'mvn verify -DskipUnitTests'
            }
        }
        stage("CODE ANALYSIS WITH CHECKSTYLE"){
             steps {
                sh 'mvn checkstyle:checkstyle'
            }
            post {
                success {
                    echo 'Generated Analysis Result'
                }
            }
        }
        stage("CODE ANALYSIS with SONARQUBE"){
        }
        stage("BUILD DOCKER IMAGES"){
        }
        stage("ROLLOUT APP"){
        }
    }
}
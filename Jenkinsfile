pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''./gradlew clean assembleDebug
test'''
        sh 'chmod 755 gradlew'
      }
    }

  }
}
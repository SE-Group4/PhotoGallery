pipeline {
  agent none
  stages {
    stage('Test') {
      steps {
        sh '''./gradlew clean assembleDebug
test'''
      }
    }

  }
}
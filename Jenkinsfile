pipeline {
  agent {
    node {
      label 'Android'
    }

  }
  stages {
    stage('Test') {
      steps {
        sh '''./gradlew clean assembleDebug
test'''
      }
    }

  }
}
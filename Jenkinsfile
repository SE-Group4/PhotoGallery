pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'chmod 755 gradlew'
        sh './gradlew clean assembleDebug test'
      }
    }

  }
}

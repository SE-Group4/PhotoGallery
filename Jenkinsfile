pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh 'chmod 755 gradlew'
        sh 'chmod +x gradlew && ./gradlew  test connectedAndroidTest'
      }
    }

  }
}
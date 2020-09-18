pipeline {
  agent {
    node {
      label 'Android'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew testDebugUnitTest testDebugUnitTest'
      }
    }

  }
}
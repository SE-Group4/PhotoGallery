pipeline {
  agent {
    node {
      label 'Android'
    }

  }
  stages {
    stage('UnitTest') {
      steps {
        sh './gradlew'
      }
    }

  }
}
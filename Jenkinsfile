@Library('my-library@current') _

pipeline {
  agent { label 'linux' }
  stages {
    stage('Test') {
      steps {
        // Let's demostrate the current git issues
        sh 'env | sort | grep GIT'
        dummy(text: 'hello world')
        sh './gradlew clean test'
      }
    }
  }
}

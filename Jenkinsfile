@Library('my-library@current') _

pipeline {
  agent { label 'linux' }
  stages {
    stage('Test') {
      steps {
        dummy(text: 'hello world')
        sh './gradlew clean test'
      }
    }
  }
}

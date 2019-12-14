def call() {
  pipeline {
    agent { label 'linux' }
    options {
      quietPeriod(10)
    }
    stages {
      stage('Build') {
        steps {
          dir('local'){
            sh 'echo "make build"'
          }
        }
      }
      stage('Release') {
        when {
          branch 'master'
        }
        steps {
          dir('local'){
            sh 'echo "make release"'
          }
        }
      }
    }
  }
}

NAME = 'it/dummy'
DSL = '''pipeline {
  agent any
  stages {
    stage('dummy step with default') {
      steps { dummy() }
    }
    stage('dummy step with some text') {
      steps { dummy(text: 'message') }
    }
  }
}'''

pipelineJob(NAME) {
  definition {
    cps {
      script(DSL.stripIndent())
    }
  }
}

// If required to be triggered automatically
queue(NAME)

import base.SpockTestBase
import com.ableton.JenkinsMocks

class MyPipelineSpecification extends SpockTestBase {

  def stepName = 'vars/myPipeline.groovy'

  def 'when running the pipeline'() {
    given:
    def script = loadScript(stepName)

    when: 'myPipeline step runs'
    JenkinsMocks.addShMock('echo "make build"', 'make build', 0)
    JenkinsMocks.addShMock('echo "make release"', 'make release', 0)
    script.call()
    printCallStack()

    then: 'the make build should be executed'
    assert(methodCallContains('sh', 'echo "make build"'))
    assertJobStatusSuccess()
  }

  def 'when running the pipeline with the master branch'() {
    given:
    def script = loadScript(stepName)

    when: 'myPipeline step runs in the master branch'
    env.BRANCH_NAME = 'master'
    JenkinsMocks.addShMock('echo "make build"', 'make build', 0)
    JenkinsMocks.addShMock('echo "make release"', 'make release', 0)
    script.call()
    printCallStack()

    then: 'the make build and release should be executed'
    assert(methodCallContains('sh', 'echo "make build"'))
    assert(methodCallContains('sh', 'echo "make release"'))
    assertJobStatusSuccess()
  }
}

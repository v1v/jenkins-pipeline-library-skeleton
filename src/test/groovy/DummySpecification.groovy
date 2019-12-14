import base.SpockTestBase

class DummySpecification extends SpockTestBase {

  def stepName = 'vars/dummy.groovy'

  def 'handling with some text option'() {
    given:
    def script = loadScript(stepName)

    when: 'dummy step is run with some text'
    script.call(text: 'dummy')
    printCallStack()

    then: "the echo should print 'dummy'"
    assert(methodCallContains('echo', 'dummy'))
    assertJobStatusSuccess()
  }

  def 'handling without any argument'() {
    given:
    def script = loadScript(stepName)

    when: 'dummy step is run'
    script.call()
    printCallStack()

    then: "the echo should print 'sample text'"
    assert(methodCallContains('echo', 'sample text'))
    assertJobStatusSuccess()
  }

  def 'Error will be thrown if running on windows'() {
    given: 'running in windows'
    def script = loadScript(stepName)
    helper.registerAllowedMethod('isUnix', [], { return false })

    when: 'dummy step is run'
    script.call(text: 'dummy')
    printCallStack()

    then: 'an Exception being thrown'
		thrown(Exception)
  }
}

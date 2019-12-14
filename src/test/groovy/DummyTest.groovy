import base.TestBase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static org.junit.Assert.assertTrue

class DummyTest extends TestBase {
  final String stepName = 'vars/dummy.groovy'

  @Rule
  public ExpectedException thrown = ExpectedException.none()

  @Override
  @Before
  void setUp() {
    super.setUp()
  }

  @Test
  void test_with_dummy_text() throws Exception {
    def script = loadScript(stepName)
    script.call(text: 'dummy')
    printCallStack()
    assertTrue(methodCallContains('echo', 'dummy'))
    assertJobStatusSuccess()
  }

  @Test
  void test_without_argument() throws Exception {
    def script = loadScript(stepName)
    script.call()
    printCallStack()
    assertTrue(methodCallContains('echo', 'sample text'))
    assertJobStatusSuccess()
  }

  @Test
  void test_in_windows() throws Exception {
    def script = loadScript(stepName)
    helper.registerAllowedMethod('isUnix', [], { return false })
    thrown.expect(Exception.class)
    thrown.expectMessage('Windows is not supported')
    script.call(text: 'dummy')
    printCallStack()
    assertJobStatusFailure()
  }
}

import base.TestBase
import com.ableton.JenkinsMocks
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class MyPipelineTest extends TestBase {
  final String stepName = 'vars/myPipeline.groovy'

  @Override
  @Before
  void setUp() {
    super.setUp()
    JenkinsMocks.addShMock('echo "make build"', 'make build', 0)
    JenkinsMocks.addShMock('echo "make release"', 'make release', 0)
  }

  @Test
  void test_pipeline() throws Exception {
    def script = loadScript(stepName)
    script.call()
    printCallStack()
    assertTrue(methodCallContains('stage', 'Build'))
    assertTrue(methodCallContains('sh', 'echo "make build"'))
    assertTrue(methodCallContains('stage', 'Release'))
    assertFalse(methodCallContains('sh', 'echo "make release"'))
    assertJobStatusSuccess()
  }

  @Test
  void test_pipeline_with_() throws Exception {
    def script = loadScript(stepName)
    env.BRANCH_NAME = 'master'
    script.call()
    printCallStack()
    assertTrue(methodCallContains('stage', 'Build'))
    assertTrue(methodCallContains('sh', 'echo "make build"'))
    assertTrue(methodCallContains('stage', 'Release'))
    assertTrue(methodCallContains('sh', 'echo "make release"'))
    assertJobStatusSuccess()
  }
}

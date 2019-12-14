package base

import com.ableton.JenkinsMocks
import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.After
import org.junit.Before

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString

class TestBase extends BasePipelineTest {

  @Override
  @Before
  void setUp() {
    super.setUp()

    // scripted steps with JenkinsMocks
    helper.with {
      registerAllowedMethod('dir', [String, Closure], JenkinsMocks.dir)
      registerAllowedMethod('error', [String], JenkinsMocks.error)
      registerAllowedMethod('pwd', [Map], JenkinsMocks.pwd)
      registerAllowedMethod('pwd', [], JenkinsMocks.pwd)
      registerAllowedMethod('retry', [int, Closure], JenkinsMocks.retry)
      registerAllowedMethod('sh', [Map], JenkinsMocks.sh)
      registerAllowedMethod('sh', [String], JenkinsMocks.sh)
    }

    // scripted steps
    helper.with {
      registerAllowedMethod('deleteDir', [], null)
      registerAllowedMethod('isUnix', [], { return true })
      registerAllowedMethod('writeFile', [Map], null)
    }

    // shared library steps
    helper.with {
      registerAllowedMethod('dummy', [Map], null)
    }
  }

  @After
  void tearDown() throws Exception {
    JenkinsMocks.clearStaticData()
  }

  boolean methodCallContains(String methodName, String compareWith) {
    return helper.callStack.findAll { call -> call.methodName == methodName }
                           .any { call -> callArgsToString(call).contains(compareWith) }
  }
}

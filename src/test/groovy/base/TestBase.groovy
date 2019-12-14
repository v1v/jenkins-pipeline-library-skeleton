package base

import com.ableton.JenkinsMocks
import com.lesfurets.jenkins.unit.BasePipelineTest
import org.junit.After
import org.junit.Before

import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString

class TestBase extends BasePipelineTest {
  Map env = [:]

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

    // declarative steps
    helper.with {
      registerAllowedMethod('agent', [Closure], null)
      registerAllowedMethod('label', [String], null)
      registerAllowedMethod('options', [Closure], { body -> body() })
      registerAllowedMethod('pipeline', [Closure], null)
      registerAllowedMethod('quietPeriod', [Integer], null)
      registerAllowedMethod('stage', [String, Closure], { stageName, bodyStage ->
        def stageResult
        registerAllowedMethod('when', [Closure], { bodyWhen ->
          registerAllowedMethod('branch', [String], { branchName  ->
            if (branchName == env.BRANCH_NAME) {
              return true
            }
            throw new RuntimeException("Stage \"${stageName}\" skipped due to when conditional")
          })
          return bodyWhen()
        })
        try {
          stageResult = bodyStage()
        }
        catch (RuntimeException re) {
          // skip stage due to when conditional
        }
        catch (Exception e) {
          throw e
        }
        return stageResult
      })
      registerAllowedMethod('stages', [Closure], null)
      registerAllowedMethod('steps', [Closure], { body -> body() })
    }

    // shared library steps
    helper.with {
      registerAllowedMethod('dummy', [Map], null)
    }

    binding.setVariable('env', env)
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

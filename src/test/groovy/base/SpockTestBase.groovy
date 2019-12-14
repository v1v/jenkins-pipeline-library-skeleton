package base

import spock.lang.Specification

class SpockTestBase extends Specification {

  @Delegate TestBase testBase

  def setup() {
    testBase = new TestBase()
    testBase.setUp()
  }
}

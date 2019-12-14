def call(Map params = [:]) {
  if (!isUnix()) {
    error 'Windows is not supported'
  }
  def text = params.containsKey('text') ? params.text : 'sample text'
  echo('I am a dummy step - ' + text)
}

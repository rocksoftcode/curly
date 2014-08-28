package com.rocksoft.curly

import spock.lang.Specification

class CurlyIntegrationSpec extends Specification {

  private static final String HOST = "http://localhost:8088"

  def "Gets 404 response"() {
    expect:
    Curly.forStatus("$HOST/not-here") == 404
  }

  def "Gets text"() {
    expect:
    Curly.forText("$HOST").replaceAll("[\n\r]", "") == new File("src/test/resources/hello.html").text.replaceAll("[\n\r]", "")
  }

  def "Gets response"() {
    when:
    CurlResponse response = Curly.forResponse("$HOST", CurlField.HTTP_STATUS_CODE, CurlField.CONTENT_TYPE)

    then:
    response.body.replaceAll("[\n\r]", "") == new File("src/test/resources/hello.html").text.replaceAll("[\n\r]", "")
    response.fields[CurlField.HTTP_STATUS_CODE] == "200"
    response.fields[CurlField.CONTENT_TYPE] == "text/html"
  }
}

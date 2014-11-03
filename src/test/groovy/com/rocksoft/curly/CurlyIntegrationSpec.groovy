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
    response.body.replaceAll("[\n\r]", "").trim() == new File("src/test/resources/hello.html").text.replaceAll("[\n\r]", "").trim()
    response.fields[CurlField.HTTP_STATUS_CODE] == "200"
    response.fields[CurlField.CONTENT_TYPE] == "text/html"
  }

  def "Gets head response"() {
    when:
    CurlHeadResponse response = Curly.forHead("$HOST")

    then:
    response.httpStatusCode == 200
    response.getHeader(HttpHeader.CONTENT_TYPE).first() == "text/html"
    response.getHeader(HttpHeader.CONTENT_LENGTH).first().isNumber()
  }

  def "Sends User Agent as an extra header"() {
    when:
    CurlResponse response = Curly.forResponse("$HOST/userAgent.jsp", [(HeaderField.USER_AGENT.value): "Test User Agent"], CurlField.HTTP_STATUS_CODE)

    then:
    response
    response.body.replaceAll("[\n\r]", "").trim() == new File("src/test/resources/userAgent.html").text.replaceAll("[\n\r]", "").trim()
  }
}

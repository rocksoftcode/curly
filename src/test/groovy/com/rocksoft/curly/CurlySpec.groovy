package com.rocksoft.curly

import spock.lang.Specification

class CurlySpec extends Specification {

  def "Normalizes URLs"() {
    expect:
    Curly.normalizeUrl(url) == normalizedUrl

    where:
    url                                          | normalizedUrl
    "http://www.rocksoftcode.com#/test"          | "http://www.rocksoftcode.com/test"
    "http://www.somespaces.com/spaced out stuff" | "http://www.somespaces.com/spaced%20out%20stuff"
  }

  def "Gets status 'line'"() {
    expect:
    Curly.readStatusLine(response) == statusLine

    where:
    response                             | statusLine
    "<html></html>\n400 text/html"       | "400 text/html"
    '''<html></html>\n\n200'''           | "200"
    "200 text/html 801039"               | "200 text/html 801039"
    "200 text/html;charset=utf-8 801039" | "200 text/html;charset=utf-8 801039"
    "301"                                | "301"
    "</someXml>\n 200"                   | "200"
    null                                 | null
  }

  def "Cleans up charset on content-type"() {
    expect:
    Curly.readStatusLine(response) == statusLine

    where:
    response                                | statusLine
    "200 text/html; charset=utf-8 801039"   | "200 text/html;charset=utf-8 801039"
    "200 text/html;   charset=utf-8 801039" | "200 text/html;charset=utf-8 801039"
    "200 text/foo;   charset=utf-8 801039"  | "200 text/foo;charset=utf-8 801039"
  }

  def "Parses a HEAD response into an object"() {
    setup:
    String mockResponseText = new File("src/test/resources/simple-head-response.txt").text

    when:
    CurlHeadResponse response = Curly.parseHeadResponse(mockResponseText)

    then:
    response.httpStatusCode == 200
    response.getHeader(HttpHeader.CONTENT_TYPE) == ['text/html; charset=utf-8']
    response.getHeader(HttpHeader.CONTENT_LENGTH) == ['849']
    response.getHeader(HttpHeader.SET_COOKIE) == ['BCSI-CS-8caa5bd26cfb5782=2; Path=/']
    response.getHeader('Cache-Control') == ['no-cache']
    response.getHeader('Pragma') == ['no-cache']
    response.getHeader('Connection') == ['close']
  }

  def "Parses all entries in a chained HEAD response into an object, returning last status"() {
    setup:
    String mockResponseText = new File("src/test/resources/chained-head-response.txt").text

    when:
    CurlHeadResponse response = Curly.parseHeadResponse(mockResponseText)

    then:
    response.httpStatusCode == 200
    response.getHeader(HttpHeader.CONTENT_TYPE) == ["text/html; charset=UTF-8", "text/html; charset=UTF-8", "text/html; charset=utf-8"]
    response.getHeader(HttpHeader.CONTENT_LENGTH) == ['849']
    response.getHeader(HttpHeader.SET_COOKIE) == ['BCSI-CS-8caa5bd26cfb5782=2; Path=/']
    response.getHeader('Cache-Control') == ["private, max-age=0", "private, max-age=0", "no-cache"]
    response.getHeader('Pragma') == ['no-cache']
    response.getHeader('Connection') == ['Keep-Alive', 'Keep-Alive', 'close']
  }

  def "Returns location field and date correctly"() {
    setup:
    String mockResponseText = new File("src/test/resources/simple-head-response-2.txt").text

    when:
    CurlHeadResponse response = Curly.parseHeadResponse(mockResponseText)

    then:
    response.getHeader(HttpHeader.LOCATION).size() == 1
    response.getHeader(HttpHeader.LOCATION).first() == "http://corporate.target.com?ref=sr_shorturl_about"
    response.getHeader(HttpHeader.DATE).size() == 1
    response.getHeader(HttpHeader.DATE).first() == "Thu, 09 Oct 2014 20:44:50 GMT"
  }

  def "Convert headers map in curl command line flags"() {
    setup:
    Map<HeaderField,String> headers = [(HeaderField.USER_AGENT.value): "Test User Agent", "Custom-Header": "Custom Header"]

    expect:
    Curly.getHeaderFlags(headers) == ["-H", "User-Agent: Test User Agent", "-H", "Custom-Header: Custom Header"]
  }
}
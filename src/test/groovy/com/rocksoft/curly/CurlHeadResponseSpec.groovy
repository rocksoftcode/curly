package com.rocksoft.curly

import spock.lang.Specification

class CurlHeadResponseSpec extends Specification {

  def "Gets header values"() {
    setup:
    CurlHeadResponse response = new CurlHeadResponse()
    response.addHeader('Foo-Bar', 'foobar')
    response.addHeader('Foo-Bar', 'foobar2')
    response.addHeader(HttpHeader.CONTENT_TYPE, 'text/plain')

    when:
    List<String> headers = response.getHeader('Foo-Bar')

    then:
    headers.size() == 2
    headers.contains('foobar')
    headers.contains('foobar2')

    when:
    headers = response.getHeader(HttpHeader.CONTENT_TYPE)

    then:
    headers == ['text/plain']
  }
}

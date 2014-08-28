package com.rocksoft.curly

import spock.lang.Specification

class CurlySpec extends Specification {

  def "Normalizes URLs"() {
    when:
    String url = Curly.normalizeUrl("http://www.rocksoft.com#/test")

    then:
    url == "http://www.rocksoft.com/test"

    when:
    url = Curly.normalizeUrl("http://www.somespaces.com/spaced out stuff")

    then:
    url == "http://www.somespaces.com/spaced%20out%20stuff"
  }

  def "Gets status 'line'"() {
    when:
    String statusLine = Curly.readStatusLine("<html></html>400 text/html")

    then:
    statusLine == "400 text/html"

    when:
    statusLine = Curly.readStatusLine('''<html></html>

200''')

    then:
    statusLine == "200"

    when:
    statusLine = Curly.readStatusLine("foobar")

    then:
    statusLine == null
  }
}
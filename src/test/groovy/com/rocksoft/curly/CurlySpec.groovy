package com.rocksoft.curly

import spock.lang.Specification

class CurlySpec extends Specification {

  def "Normalizes URLs"() {
    when:
    String url = Curly.normalizeUrl("http://www.rocksoftcode.com#/test")

    then:
    url == "http://www.rocksoftcode.com/test"

    when:
    url = Curly.normalizeUrl("http://www.somespaces.com/spaced out stuff")

    then:
    url == "http://www.somespaces.com/spaced%20out%20stuff"
  }

  def "Gets status 'line'"() {
    when:
    String statusLine = Curly.readStatusLine("<html></html>\n400 text/html")

    then:
    statusLine == "400 text/html"

    when:
    statusLine = Curly.readStatusLine('''<html></html>\n\n200''')

    then:
    statusLine == "200"

    when:
    statusLine = Curly.readStatusLine("200 text/html 801039")

    then:
    statusLine == "200 text/html 801039"

    when:
    statusLine = Curly.readStatusLine("301")

    then:
    statusLine == "301"

    when:
    statusLine = Curly.readStatusLine("</someXml>\n 200")

    then:
    statusLine == "200"

    when:
    statusLine = Curly.readStatusLine(null)

    then:
    statusLine == null
  }
}
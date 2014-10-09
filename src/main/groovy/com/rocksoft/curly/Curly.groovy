package com.rocksoft.curly

import java.util.regex.Matcher

class Curly {

  private static final String CURL = "curl"
  private static final String[] FLAGS = ['-s', '-L', '-m', '60']
  private static final Boolean INSECURE = Boolean.TRUE

  static Integer forStatus(String url) {
    String response = doCurl(url, CurlField.HTTP_STATUS_CODE)
    String statusLine = readStatusLine(response)
    if (statusLine) {
      return statusLine.toInteger()
    }

    return null
  }

  static String forText(String url) {
    return doCurl(url)
  }

  static CurlResponse forResponse(String url, CurlField... fields) {
    String response = doCurl(url, fields)
    String statusLine = readStatusLine(response)
    Map<CurlField, String> responseFields = [:]
    statusLine.split(' ').eachWithIndex{ String value, int i ->
      responseFields[fields[i]] = value
    }
    String body = response.substring(0, response.length() - statusLine.length())
    return new CurlResponse(body: body, fields: responseFields)
  }

  private static String doCurl(String url, CurlField... fields) {
    List<String> command = [CURL]
    command.addAll(FLAGS)
    if (fields) {
      command << '-w'
      command << '\\n ' + fields*.value().collect { "%{$it}" }.join(' ')
    }
    command << normalizeUrl(url)
    if (INSECURE) {
      command << "--insecure"
    }
    return command.execute().in.text
  }

  static String normalizeUrl(String url) {
    return url.replaceAll('#', '').replaceAll(' ', '%20')
  }

  static String readStatusLine(String response) {
    if (!response) {
      return null
    }
    String lastLine = response.readLines().last().trim()
    if (lastLine.contains(" charset=")) {
      Matcher spaceBeforeCharset = (lastLine =~ /;\s+charset=/)
      lastLine = spaceBeforeCharset.replaceAll(';charset=')
    }
    return lastLine
  }

  static CurlHeadResponse forHead(String url) {
    List<String> command = [CURL]
    command.addAll(FLAGS)
    command << '-I'
    command << normalizeUrl(url)
    if (INSECURE) {
      command << "--insecure"
    }
    return parseHeadResponse(command.execute().in.text)
  }

  static CurlHeadResponse parseHeadResponse(String text) {
    CurlHeadResponse response = new CurlHeadResponse()

    text.eachLine { String line ->
      String[] parts = line.split(':')
      if (parts.length == 1) {
        if (line.contains("HTTP/")) {
          response.httpStatusCode = line.split(' ')[1].toInteger()
        }
      } else if (parts.length > 2) {
        response.addHeader(parts.first(), parts[1..<parts.size()].join(':').trim())
      } else {
        response.addHeader(parts.first(), parts.last().trim())
      }
    }

    return response
  }
}

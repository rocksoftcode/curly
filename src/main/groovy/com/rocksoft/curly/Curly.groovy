package com.rocksoft.curly

class Curly {

  private static final String CURL = "curl"
  private static final String[] FLAGS = ['-s', '-L']
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

    return response.readLines().last().trim()
  }
}

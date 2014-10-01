package com.rocksoft.curly

class CurlHeadResponse {
  int httpStatusCode
  private List<Map<String, String>> headers = []

  List<String> getHeader(HttpHeader header) {
    return getHeader(header.value())
  }

  List<String> getHeader(String header) {
    return headers.findAll { it.keySet().contains(header) }.collect { it.values().first() }
  }

  void addHeader(HttpHeader header, String value) {
    addHeader(header.value(), value)
  }

  void addHeader(String header, String value) {
    headers << [(header): value]
  }
}

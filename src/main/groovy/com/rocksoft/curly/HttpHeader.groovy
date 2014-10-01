package com.rocksoft.curly

public enum HttpHeader {
  CONTENT_TYPE("Content-Type"), LOCATION("Location"), DATE("Date"), EXPIRES("Expires"),
  CONTENT_LENGTH("Content-Length"), SET_COOKIE("Set-Cookie"), PRAGMA("Pragma")

  String value

  HttpHeader(String value) {
    this.value = value
  }

  String value() {
    return value
  }
}
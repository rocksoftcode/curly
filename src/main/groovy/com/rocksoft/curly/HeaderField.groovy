package com.rocksoft.curly

public enum HeaderField {

  USER_AGENT("User-Agent")

  String value

  HeaderField(String value) {
    this.value = value
  }

  String value() {
    return value
  }
}
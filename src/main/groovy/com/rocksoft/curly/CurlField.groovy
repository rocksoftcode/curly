package com.rocksoft.curly

public enum CurlField {

  HTTP_STATUS_CODE("http_code"),
  CONTENT_TYPE("content_type"),
  LOCAL_IP("local_ip"),
  LOCAL_PORT("local_port"),
  NUMBER_OF_CONNECTS("num_connects"),
  NUMBER_OF_REDIRECTS("num_redirects"),
  REDIRECT_URL("redirect_url"),
  REMOTE_IP("remote_ip"),
  REMOTE_PORT("remote_port"),
  DOWNLOAD_SIZE("size_download"),
  HEADER_SIZE("size_header"),
  REQUEST_SIZE("size_request"),
  DOWNLOAD_SPEED("speed_download"),
  INIT_TIME("time_appconnect"),
  CONNECT_TIME("time_connect"),
  LOOKUP_TIME("time_namelookup"),
  PRETRANSFER_TIME("time_pretransfer"),
  REDIRECT_TIME("time_redirect"),
  START_TRANSFER_TIME("time_starttransfer"),
  TOTAL_TIME("time_total"),
  EFFECTIVE_URL("url_effective")

  String value

  CurlField(String value) {
    this.value = value
  }

  String value() {
    return value
  }
}
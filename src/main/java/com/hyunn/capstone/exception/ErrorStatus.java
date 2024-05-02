package com.hyunn.capstone.exception;

public enum ErrorStatus {
  // 400 BAD_REQUEST 잘못된 요청
  // 401 Unauthorized 인증되지 않음
  // 403 Forbidden 권한 없음
  // 404 NOT_FOUND 잘못된 리소스 접근
  // 405 METHOD_NOT_ALLOWED 지원되지 않는 HTTP 요청 메서드
  // 500 INTERNAL SERVER ERROR 서버 오류
  INVALID_PARAMETER("01"),
  NEED_MORE_PARAMETER("02"),
  MEDIA_TYPE_NOT_SUPPORTED_EXCEPTION("03"),
  USER_NOT_FOUND_EXCEPTION("04"),
  METHOD_NOT_ALLOWED_EXCEPTION("05"),
  DATABASE_ERROR("06"),
  INTERNAL_SERVER_ERROR("07"),
  VALIDATION_EXCEPTION("08"),
  INVALID_JSON_EXCEPTION("09"),
  API_NOT_FOUND_EXCEPTION("10"),
  IMAGE_NOT_FOUND_EXCEPTION("11"),
  AUTHENTICATION_EXCEPTION("12"),
  ROOT_USER_EXCEPTION("13"),
  S3_UPLOAD_EXCEPTION("14"),
  NEED_MORE_PART_EXCEPTION("15"),
  FILE_SIZE_EXCEED_EXCEPTION("16"),
  FILE_NOT_ALLOWED_EXCEPTION("17");

  private final String code;

  ErrorStatus(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }
}

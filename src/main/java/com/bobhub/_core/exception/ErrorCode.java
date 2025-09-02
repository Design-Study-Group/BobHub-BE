package com.bobhub._core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  // 400 BAD_REQUEST
  INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.", "AUTH_002"),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력 값입니다.", "COMMON_001"),
  METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "잘못된 인자 값입니다.", "COMMON_002"),

  // 401 UNAUTHORIZED
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.", "AUTH_001"),
  TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다.", "AUTH_003"),

  // 403 FORBIDDEN
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "AUTH_004"),

  // 404 NOT_FOUND
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.", "USER_001"),
  RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다.", "POST_001"),
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다.", "COMMENT_001"),

  // 409 CONFLICT
  DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "데이터 무결성 위반입니다.", "COMMON_003"),

  // 500 INTERNAL_SERVER_ERROR
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.", "SERVER_001");

  private final HttpStatus status;
  private final String message;
  private final String code;
}

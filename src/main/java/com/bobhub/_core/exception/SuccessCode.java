package com.bobhub._core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode {
  // 200 OK
  OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),

  // 201 CREATED
  CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다.");

  private final HttpStatus status;
  private final String message;
}

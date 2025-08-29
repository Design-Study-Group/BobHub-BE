package com.bobhub._core.utils;

import com.bobhub._core.exception.ErrorCode;
import com.bobhub._core.exception.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private final int status;
  private final String message;
  private final T data;

  public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
    return new ApiResponse<>(successCode.getStatus().value(), successCode.getMessage(), data);
  }

  public static ApiResponse<?> error(ErrorCode errorCode, String message) {
    return new ApiResponse<>(errorCode.getStatus().value(), message, null);
  }
}

package com.bobhub._core.utils;

import com.bobhub._core.exception.ErrorCode;
import com.bobhub._core.exception.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ApiUtils {

  public static <T> ApiResult<T> success(T response, SuccessCode successCode) {
    return new ApiResult<>(true, response, successCode.getMessage(), successCode.getCode());
  }

  public static ApiResult<?> success(SuccessCode successCode) {
    return new ApiResult<>(true, null, successCode.getMessage(), successCode.getCode());
  }

  public static ApiResult<?> error(ErrorCode errorCode) {
    return new ApiResult<>(false, null, errorCode.getMessage(), errorCode.getCode());
  }

  @Getter
  @AllArgsConstructor
  public static class ApiResult<T> {
    private final boolean success;
    private final T response;
    private final String message;
    private final String code;
  }
}

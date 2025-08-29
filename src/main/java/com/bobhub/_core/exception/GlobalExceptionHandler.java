package com.bobhub._core.exception;

import com.bobhub._core.utils.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException", e);
    return ResponseEntity.status(ErrorCode.METHOD_ARGUMENT_NOT_VALID.getStatus())
        .body(
            ApiResponse.error(
                ErrorCode.METHOD_ARGUMENT_NOT_VALID,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponse<?>> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException", e);
    return ResponseEntity.status(ErrorCode.DATA_INTEGRITY_VIOLATION.getStatus())
        .body(ApiResponse.error(ErrorCode.DATA_INTEGRITY_VIOLATION, e.getMessage()));
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
    log.error("CustomException", e);
    return ResponseEntity.status(e.getErrorCode().getStatus())
        .body(ApiResponse.error(e.getErrorCode(), e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
    log.error("Exception", e);
    return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
        .body(ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()));
  }
}

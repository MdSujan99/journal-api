package com.mds.journal_app.exceptions;

import com.mds.journal_app.pojo.BaseApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<BaseApiResponse<String>> handleApiException(ApiException ex) {
    BaseApiResponse<String> response = BaseApiResponse.error(ex.getErrorCode(), ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseApiResponse<String>> handleGenericException(Exception ex) {
    BaseApiResponse<String> response =
        BaseApiResponse.error(500, "An unexpected error occurred: " + ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

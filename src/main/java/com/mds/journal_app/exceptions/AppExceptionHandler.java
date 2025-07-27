package com.mds.journal_app.exceptions;

import com.mds.journal_app.pojo.AppExceptionResponse;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
  @ExceptionHandler(AppException.class)
  public ResponseEntity<AppExceptionResponse> handleAppExceptions(AppException ex) {
    log.error("AppException occurred - ", ex);
    AppExceptionResponse appException =
            AppExceptionResponse.builder()
                    .timestamp(Instant.now())
                    .message(ex.getMessage())
                    .code(ex.getErrorCode())
                    .build();
    return ResponseEntity.status(ex.getErrorCode()).body(appException);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<AppExceptionResponse> handleAllExceptions(RuntimeException ex) {
    log.error("AppException occurred - ", ex);
    AppExceptionResponse appException =
        AppExceptionResponse.builder()
            .timestamp(Instant.now())
            .message(ex.getMessage())
            .code(500)
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(appException);
  }
}

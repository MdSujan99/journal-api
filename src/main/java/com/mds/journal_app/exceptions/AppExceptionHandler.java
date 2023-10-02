package com.mds.journal_app.exceptions;

import com.mds.journal_app.pojo.AppExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler {
    @ExceptionHandler(JournalNotFoundException.class)
    public ResponseEntity<AppExceptionResponse> handleJournalNotFoundException(JournalNotFoundException ex) {
        log.error("AppException occurred - ", ex);
        AppExceptionResponse appException = AppExceptionResponse.builder()
                .timestamp(Instant.now())
                .message("no journals found")
                .code(400).build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(appException);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<AppExceptionResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("AppException occurred - ", ex);
        AppExceptionResponse appException = AppExceptionResponse.builder()
                .timestamp(Instant.now())
                .message("duplicate record error")
                .code(400).build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(appException);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<AppExceptionResponse> handleAllExceptions(Throwable ex) {
        log.error("AppException occurred - ", ex);
        AppExceptionResponse appException = AppExceptionResponse.builder()
                .timestamp(Instant.now())
                .message(ex.getMessage())
                .code(400).build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(appException);
    }

    // You can define more exception handlers for other exception types here

}

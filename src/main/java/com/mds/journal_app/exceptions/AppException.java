package com.mds.journal_app.exceptions;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AppException extends RuntimeException {
  String message;
  int errorCode;

  public AppException(String message, int errorCode) {
    this.message = message;
    this.errorCode = errorCode;
  }
}

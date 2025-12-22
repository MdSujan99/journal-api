package com.mds.journal_app.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiException extends RuntimeException {
  String message;
  int errorCode;

  public ApiException(String message, int errorCode) {
    super(message);
    this.message = message;
    this.errorCode = errorCode;
  }
}

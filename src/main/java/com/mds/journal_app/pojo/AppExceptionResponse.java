package com.mds.journal_app.pojo;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AppExceptionResponse {
  Instant timestamp;
  String message;
  Integer code;
}

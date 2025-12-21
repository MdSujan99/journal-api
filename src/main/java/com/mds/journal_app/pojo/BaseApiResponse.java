package com.mds.journal_app.pojo;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BaseApiResponse<T> {
  Instant timestamp;
  int status;
  String message;
  T payload;

  public static <T> BaseApiResponse<T> success(int status, String message, T payload) {
    return BaseApiResponse.<T>builder()
        .status(status)
        .message(message)
        .timestamp(Instant.now())
        .payload(payload)
        .build();
  }

  public static <T> BaseApiResponse<T> error(int status, String message) {
    return BaseApiResponse.<T>builder()
        .status(status)
        .message(message)
        .timestamp(Instant.now())
        .payload(null)
        .build();
  }
}

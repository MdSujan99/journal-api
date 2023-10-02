package com.mds.journal_app.pojo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AppExceptionResponse {
    Instant timestamp;
    String message;
    Integer code;
}

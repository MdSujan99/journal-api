package com.mds.journal_app.pojo;

import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalEntryResponse {
  String textContent;
  Instant dateCreated;
}

package com.mds.journal_app.pojo;

import java.time.Instant;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalResponse {
  String id;
  String title;
  String description;
  Instant createdAt;
  Instant updatedAt;
  Map<String, JournalEntryResponse> journalEntryMap;
}

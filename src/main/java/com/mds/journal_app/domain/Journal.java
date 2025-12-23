package com.mds.journal_app.domain;

import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Journal {
  String id;

  String title;

  String description;

  Instant createdAt;

  Instant updatedAt;

  List<JournalEntry> journalEntryMap;
}

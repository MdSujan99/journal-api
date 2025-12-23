package com.mds.journal_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "journals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalSQL {
  @JsonIgnore @Id String id;

  String title;

  String description;

  Instant createdAt;

  Instant updatedAt;

  @JsonIgnore List<JournalEntry> journalEntryMap;
}

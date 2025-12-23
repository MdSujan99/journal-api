package com.mds.journal_app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("journal")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalMongo {
  @JsonIgnore @Id String id;

  @Indexed(unique = true)
  String title;

  String description;

  @CreatedDate
  @Field("createdAt")
  Instant createdAt;

  @LastModifiedDate
  @Field("updatedAt")
  Instant updatedAt;

  @JsonIgnore List<JournalEntry> journalEntryMap;
}

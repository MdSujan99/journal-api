package com.mds.journal_app.pojo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostJournalRequest {
    String title;
    String description;
    Instant dateCreated;
}

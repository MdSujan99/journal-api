package com.mds.journal_app.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalEntryRequest {
  String textContent;
}

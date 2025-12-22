package com.mds.journal_app;

import com.mds.journal_app.pojo.JournalResponse;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {
  public List<JournalResponse> getSampleJournals() {
    List<JournalResponse> journals = new ArrayList<>();

    journals.add(
        JournalResponse.builder()
            .id("1")
            .title("Sample Journal 1")
            .description("This is a sample journal description 1.")
            .build());
    journals.add(
        JournalResponse.builder()
            .id("2")
            .title("Sample Journal 2")
            .description("This is a sample journal description 2.")
            .build());
    journals.add(
        JournalResponse.builder()
            .id("3")
            .title("Sample Journal 3")
            .description("This is a sample journal description 3.")
            .build());

    return journals;
  }
}

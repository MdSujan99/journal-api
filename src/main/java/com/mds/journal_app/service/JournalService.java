package com.mds.journal_app.service;

import com.mds.journal_app.pojo.Journal;
import com.mds.journal_app.pojo.PostJournalRequest;
import com.mds.journal_app.pojo.PostJournalResponse;
import com.mds.journal_app.repo.JournalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalService {
    @Autowired
    JournalRepo journalRepo;
    public String testGet(){
        return "test success";
    }

    public PostJournalResponse postJournal(
            PostJournalRequest postJournalRequest) {
        journalRepo.save(
                Journal.builder()
                        .title(postJournalRequest.getTitle())
                        .dateCreated(postJournalRequest.getDateCreated())
                        .description(postJournalRequest.getDescription())
                        .build());
        return PostJournalResponse.builder()
                .title(postJournalRequest.getTitle())
                .description(postJournalRequest.getDescription())
                .dateCreated(postJournalRequest.getDateCreated())
                .build();
    }
}

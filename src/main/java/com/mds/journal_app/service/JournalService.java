package com.mds.journal_app.service;

import com.mds.journal_app.pojo.*;
import com.mds.journal_app.repo.JournalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JournalService {
    @Autowired
    JournalRepo journalRepo;

    public String testGet() {
        return "test success";
    }

    public PostJournalResponse postJournal(
            PostJournalRequest postJournalRequest) {
        // todo
        //  - add validations
        //  - add check for creating duplicate journal

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

    public PostJournalEntryResponse postJournalEntry(PostJournalEntryRequest postJournalEntryRequest) {
        return null;
    }

    public List<JournalEntry> getJournalEntriesByDate(Instant dateFrom, Instant dateTo) {
        return null;
    }

    public List<Journal> getAllJournals() {
        return journalRepo.findAll();
    }
}

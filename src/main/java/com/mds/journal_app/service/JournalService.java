package com.mds.journal_app.service;

import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.pojo.Journal;
import com.mds.journal_app.pojo.JournalEntry;
import com.mds.journal_app.pojo.PostJournalEntryRequest;
import com.mds.journal_app.pojo.PostJournalRequest;
import com.mds.journal_app.repo.JournalRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JournalService {
    @Autowired
    JournalRepo journalRepo;

    public String testGet() {
        return "test success";
    }

    /**
     * create a new journal
     */
    public void postJournal(
            PostJournalRequest postJournalRequest) {
        validateCreateJournal(postJournalRequest);
        journalRepo.save(
                Journal.builder()
                        .title(postJournalRequest.getTitle())
                        .description(postJournalRequest.getDescription())
                        .build());
    }

    private void validateCreateJournal(
            PostJournalRequest postJournalRequest) {
        log.info("validateCreateJournal - validations passed");
    }

    private void validateCreateJournalEntry(
            PostJournalEntryRequest postJournalEntryRequest) {
        log.info("validateCreateJournalEntry - validations passed");
    }

    public void postJournalEntry(
            String journalId,
            PostJournalEntryRequest postJournalEntryRequest) throws JournalNotFoundException {
        // find the journal by id
        Journal existingJournal = findJournalById(journalId);

        // make a new entry in its journalEntriesMap
        if (Objects.isNull(existingJournal.getJournalEntryMap())) {
            existingJournal.setJournalEntryMap(new HashMap<>());
        }
        Instant entryDate = Instant.now();
        String key = getJournalEntryKey(entryDate);
        JournalEntry journalEntry = JournalEntry.builder()
                .textContent(postJournalEntryRequest.getTextContent())
                .dateCreated(entryDate)
                .build();
        existingJournal.getJournalEntryMap().put(key, journalEntry);
        journalRepo.save(existingJournal);
    }

    private Journal findJournalById(String journalId) throws JournalNotFoundException {
        Journal existingJournal = journalRepo.findById(journalId).orElse(null);
        if (Objects.nonNull(existingJournal))
            return existingJournal;
        throw new JournalNotFoundException();
    }

    private static String getJournalEntryKey(Instant instant) {
        // Define a formatter with the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);
        return formatter.format(instant);
    }

    public List<JournalEntry> getJournalEntriesByDate(Instant dateFrom, Instant dateTo) {
        // todo
        return null;
    }

    public List<Journal> getAllJournals() {
        return journalRepo.findAll();
    }
}

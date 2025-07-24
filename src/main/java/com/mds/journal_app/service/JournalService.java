package com.mds.journal_app.service;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.dao.JournalRepo;
import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.mapper.JournalMapper;
import com.mds.journal_app.pojo.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalService {
  private final JournalRepo journalRepo;
  private final JournalMapper journalMapper;

  public void postJournal(JournalRequest journalRequest) {
    log.info("postJournal - request: {}", journalRequest);
    validateCreateJournal(journalRequest);
    journalRepo.save(
        Journal.builder()
            .title(journalRequest.getTitle())
            .description(journalRequest.getDescription())
            .build());
    log.info("postJournal - journal created successfully");
  }

  private void validateCreateJournal(JournalRequest journalRequest) {
    log.info("validateCreateJournal - validations passed");
  }

  public void postJournalEntry(String journalId, JournalEntryRequest journalEntryRequest)
      throws JournalNotFoundException {
    log.info("postJournalEntry - request: {}", journalEntryRequest);
    // find the journal by id
    Journal existingJournal = findJournalById(journalId);
    // make a new entry in its journalEntriesMap
    if (Objects.isNull(existingJournal.getJournalEntryMap())) {
      existingJournal.setJournalEntryMap(new HashMap<>());
    }
    Instant entryDate = Instant.now();
    String key = getJournalEntryKey(entryDate);
    JournalEntryResponse journalEntryResponse =
        JournalEntryResponse.builder()
            .textContent(journalEntryRequest.getTextContent())
            .dateCreated(entryDate)
            .build();
    existingJournal.getJournalEntryMap().put(key, journalEntryResponse);
    journalRepo.save(existingJournal);
    log.info("postJournalEntry - journal entry added successfully for journalId: {}", journalId);
  }

  private Journal findJournalById(String journalId) throws JournalNotFoundException {
    log.info("findJournalById - journalId: {}", journalId);
    Journal existingJournal = journalRepo.findById(journalId).orElse(null);
    if (Objects.nonNull(existingJournal)) {
      log.info("findJournalById - journal found: {}", existingJournal);
      return existingJournal;
    }
    throw new JournalNotFoundException();
  }

  private static String getJournalEntryKey(Instant instant) {
    log.info("getJournalEntryKey - instant: {}", instant);
    // Define a formatter with the desired format
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
    return formatter.format(instant);
  }

  public List<JournalEntryResponse> getJournalEntriesByDate(
      String journalId, Instant dateFrom, Instant dateTo) throws JournalNotFoundException {
    log.info(
        "getJournalEntriesByDate - journalId: {}, dateFrom: {}, dateTo: {}",
        journalId,
        dateFrom,
        dateTo);
    Journal journal = findJournalById(journalId);
    Map<String, JournalEntryResponse> journalEntryMap = journal.getJournalEntryMap();

    if (journalEntryMap == null || journalEntryMap.isEmpty()) {
      // Return an empty list if journalEntryMap is null or empty
      log.info("getJournalEntriesByDate - no journal entries found for journalId: {}", journalId);
      return Collections.emptyList();
    }

    return journalEntryMap.entrySet().stream()
        .filter(
            entry -> {
              Instant entryDate = Instant.parse(entry.getKey());
              return !entryDate.isBefore(dateFrom) && !entryDate.isAfter(dateTo);
            })
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  public List<JournalResponse> getAllJournals() {
    log.info("getAllJournals - fetching all journals");
    List<Journal> allJournals = journalRepo.findAll();
    if (allJournals.isEmpty()) {
      log.info("getAllJournals - no journals found");
      return Collections.emptyList();
    }
    log.info("getAllJournals - found {} journals", allJournals.size());

    return allJournals.stream().map(journalMapper::toJournalResponse).toList();
  }

  public void deleteJournalById(String journalId) throws JournalNotFoundException {
    log.info("deleteJournalById - journalId: {}", journalId);
    findJournalById(journalId);
    journalRepo.deleteById(journalId);
    log.info("deleteJournalById - journal deleted successfully for journalId: {}", journalId);
  }
}

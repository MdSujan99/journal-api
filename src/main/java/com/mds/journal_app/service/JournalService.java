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

  public String testGet() {
    return "test success";
  }

  /** create a new journal */
  public void postJournal(JournalRequest journalRequest) {
    validateCreateJournal(journalRequest);
    journalRepo.save(
        Journal.builder()
            .title(journalRequest.getTitle())
            .description(journalRequest.getDescription())
            .build());
  }

  private void validateCreateJournal(JournalRequest journalRequest) {
    log.info("validateCreateJournal - validations passed");
  }

  private void validateCreateJournalEntry(JournalEntryRequest journalEntryRequest) {
    log.info("validateCreateJournalEntry - validations passed");
  }

  public void postJournalEntry(String journalId, JournalEntryRequest journalEntryRequest)
      throws JournalNotFoundException {
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
  }

  private Journal findJournalById(String journalId) throws JournalNotFoundException {
    Journal existingJournal = journalRepo.findById(journalId).orElse(null);
    if (Objects.nonNull(existingJournal)) return existingJournal;
    throw new JournalNotFoundException();
  }

  private static String getJournalEntryKey(Instant instant) {
    // Define a formatter with the desired format
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
    return formatter.format(instant);
  }

  public List<JournalEntryResponse> getJournalEntriesByDate(
      String journalId, Instant dateFrom, Instant dateTo) throws JournalNotFoundException {
    Journal journal = findJournalById(journalId);
    Map<String, JournalEntryResponse> journalEntryMap = journal.getJournalEntryMap();

    if (journalEntryMap == null || journalEntryMap.isEmpty()) {
      // Return an empty list if journalEntryMap is null or empty
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
    List<Journal> allJournals = journalRepo.findAll();
    return allJournals.stream().map(journal -> journalMapper.toJournalResponse(journal)).toList();
  }

  public void deleteJournalById(String journalId) throws JournalNotFoundException {
    findJournalById(journalId);
    journalRepo.deleteById(journalId);
  }
}

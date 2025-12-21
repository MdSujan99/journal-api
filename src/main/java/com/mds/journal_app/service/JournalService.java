package com.mds.journal_app.service;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.dao.JournalRepo;
import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.mapper.JournalMapper;
import com.mds.journal_app.pojo.*;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.mds.journal_app.pojo.CommonConstants.KEY_DELIMITER;

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
  public JournalResponse postJournal(JournalRequest journalRequest) {
    validateCreateJournal(journalRequest);
    Journal journal =
        Journal.builder()
            .title(journalRequest.getTitle())
            .description(journalRequest.getDescription())
            .build();
    journalRepo.save(journal);
    return journalMapper.toJournalResponse(journal);
  }

  private void validateCreateJournal(JournalRequest journalRequest) {
    log.info("validateCreateJournal - validations passed");
    // todo add validations
  }

  private void validateCreateJournalEntry(JournalEntryRequest journalEntryRequest) {
    log.info("validateCreateJournalEntry - validations passed");
    // todo add validations
  }

  public JournalResponse postJournalEntry(String journalId, JournalEntryRequest journalEntryRequest)
      throws JournalNotFoundException {
    Instant nowTs = Instant.now();

    // find the journal by id
    Journal existingJournal = findJournalById(journalId);

    // make a new entry in its journalEntriesMap
    if (Objects.isNull(existingJournal.getJournalEntryMap())) {
      existingJournal.setJournalEntryMap(new HashMap<>());
    }

    existingJournal
        .getJournalEntryMap()
        .put(
            nowTs.toEpochMilli() + KEY_DELIMITER + journalEntryRequest.getTitle(),
            journalEntryRequest.getTextContent());

    journalRepo.save(existingJournal);

    return journalMapper.toJournalResponse(existingJournal);
  }

  private Journal findJournalById(String journalId) throws JournalNotFoundException {
    Journal existingJournal = journalRepo.findById(journalId).orElse(null);
    if (Objects.nonNull(existingJournal)) return existingJournal;
    throw new JournalNotFoundException();
  }

  public List<JournalEntryResponse> getJournalEntriesByDate(
      String journalId, Instant dateFrom, Instant dateTo) throws JournalNotFoundException {
    Journal journal = findJournalById(journalId);
    Map<String, String> journalEntryMap = journal.getJournalEntryMap();

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
        .map(
            entry ->
                JournalEntryResponse.builder()
                    .textContent(entry.getValue())
                    .dateCreated(Instant.parse(entry.getKey()))
                    .build())
        .collect(Collectors.toList());
  }

  public List<JournalResponse> getAllJournals() {
    List<Journal> allJournals = journalRepo.findAll();
    return allJournals.stream().map(journalMapper::toJournalResponse).toList();
  }

  public void deleteJournalById(String journalId) throws JournalNotFoundException {
    findJournalById(journalId);
    journalRepo.deleteById(journalId);
  }
}

package com.mds.journal_app.service;

import static com.mds.journal_app.pojo.JournalConstants.JOURNAL_NOT_FOUND_MSG;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.dao.JournalRepo;
import com.mds.journal_app.exceptions.AppException;
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
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalService {

  private final JournalRepo journalRepo;
  private final JournalMapper journalMapper;

  public PostJournalResponse postJournal(JournalRequest journalRequest) {
    log.info("postJournal - request: {}", journalRequest);
    validatePostJournal(journalRequest);
    Journal savedJournal =
        journalRepo.save(
            Journal.builder()
                .title(journalRequest.getTitle())
                .description(journalRequest.getDescription())
                .build());
    log.info("postJournal - journal saved successfully");
    return PostJournalResponse.builder()
        .journalId(savedJournal.getId())
        .message("Journal saved successfully!")
        .build();
  }

  private void validatePostJournal(JournalRequest journalRequest) {
    log.info("validateUpdateRequest initiated for journalRequest: {}", journalRequest);
    if (StringUtils.hasText(journalRequest.getId())) {
      log.info("validateUpdateRequest - journalId is present: {}", journalRequest.getId());
      validateUpdateRequest(journalRequest);
    }
    log.info("validateUpdateRequest completed for journalRequest: {}", journalRequest);
  }

  private void validateUpdateRequest(JournalRequest journalRequest) {
    // Check if a journal with the same title already exists
    Optional<Journal> existingJournal = journalRepo.findById(journalRequest.getId());
    if (existingJournal.isPresent()) {
      if (!StringUtils.hasText(journalRequest.getId())) {
        throw new AppException(JOURNAL_NOT_FOUND_MSG, 404);
      }
      // verify if the every field is same as the existing journal
      // if all fields are same, then throw an exception
      Journal journal = existingJournal.get();
      if (journal.getTitle().equals(journalRequest.getTitle())
          && Objects.equals(journal.getDescription(), journalRequest.getDescription())) {
        throw new IllegalArgumentException(
            "Journal with the same title and description already exists");
      }
    }
  }

  public void postJournalEntry(String journalId, JournalEntryRequest journalEntryRequest) {
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

  private Journal findJournalById(String journalId) {
    log.info("findJournalById - journalId: {}", journalId);
    Journal existingJournal = journalRepo.findById(journalId).orElse(null);
    if (Objects.nonNull(existingJournal)) {
      log.info("findJournalById - journal found: {}", existingJournal);
      return existingJournal;
    }
    throw new AppException(JOURNAL_NOT_FOUND_MSG, 404);
  }

  private static String getJournalEntryKey(Instant instant) {
    log.info("getJournalEntryKey - instant: {}", instant);
    // Define a formatter with the desired format
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
    return formatter.format(instant);
  }

  public List<JournalEntryResponse> getJournalEntriesByDate(
      String journalId, Instant dateFrom, Instant dateTo) {
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

  public void deleteJournalById(String journalId) {
    log.info("deleteJournalById - journalId: {}", journalId);
    findJournalById(journalId);
    journalRepo.deleteById(journalId);
    log.info("deleteJournalById - journal deleted successfully for journalId: {}", journalId);
  }
}

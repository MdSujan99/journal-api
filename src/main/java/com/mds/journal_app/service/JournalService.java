package com.mds.journal_app.service;

import static com.mds.journal_app.pojo.JournalConstants.*;

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
import org.springframework.dao.DuplicateKeyException;
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
    if (StringUtils.hasText(journalRequest.getId())) {
      return handleUpdateJournal(journalRequest);
    } else {
      return handleCreateJournal(journalRequest);
    }
  }

  private PostJournalResponse handleCreateJournal(JournalRequest journalRequest) {
    try {
      Journal journal =
          journalRepo.save(
              Journal.builder()
                  .title(journalRequest.getTitle())
                  .description(journalRequest.getDescription())
                  .createdAt(Instant.now())
                  .updatedAt(Instant.now())
                  .build());
      return PostJournalResponse.builder()
          .id(journal.getId())
          .message(JOURNAL_CREATION_SUCCESS_MSG)
          .build();
    } catch (DuplicateKeyException e) {
      log.error("postJournal - Duplicate key error while creating journal: {}", e.getMessage());
      throw new AppException("Journal with the same title already exists", 400);
    }
  }

  private PostJournalResponse handleUpdateJournal(JournalRequest journalRequest) {
    try {
      log.info("postJournal - updating existing journal with id: {}", journalRequest.getId());
      Optional<Journal> existingJournal = journalRepo.findById(journalRequest.getId());
      if (existingJournal.isPresent()) {
        log.info("postJournal - updating existing journal with id: {}", journalRequest.getId());
        Journal journal = existingJournal.get();
        journal.setTitle(journalRequest.getTitle());
        journal.setDescription(journalRequest.getDescription());
        journalRepo.save(journal);
        return PostJournalResponse.builder()
            .id(journal.getId())
            .message("Journal updated successfully")
            .build();
      } else {
        throw new AppException(JOURNAL_NOT_FOUND_MSG, 404);
      }
    } catch (DuplicateKeyException e) {
      log.error("postJournal - Duplicate key error while updating journal: {}", e.getMessage());
      throw new AppException("Journal with the same title already exists", 400);
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

package com.mds.journal_app.service;

import com.mds.journal_app.dao.JournalDao;
import com.mds.journal_app.domain.Journal;
import com.mds.journal_app.domain.JournalEntry;
import com.mds.journal_app.exceptions.ApiException;
import com.mds.journal_app.mapper.JournalMapper;
import com.mds.journal_app.pojo.*;
import java.time.Instant;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JournalService {
  private final JournalDao journalDao;
  private final JournalMapper journalMapper;

  /** create a new journal */
  public JournalResponse postJournal(JournalRequest journalRequest) {
    validateCreateJournal(journalRequest);
    Journal journal = journalMapper.fromJournalReqToJournal(journalRequest);
    journalDao.saveJournal(journal);
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

  public JournalResponse postJournalEntry(
      String journalId, JournalEntryRequest journalEntryRequest) {
    Instant nowTs = Instant.now();

    // find the journal by id
    Journal existingJournal = findJournalById(journalId);

    // make a new entry in its journalEntriesMap
    if (Objects.isNull(existingJournal.getJournalEntryMap())) {
      existingJournal.setJournalEntryMap(new ArrayList<>());
    }

    existingJournal
        .getJournalEntryMap()
        .add(
            JournalEntry.builder()
                .entryContent(journalEntryRequest.getTextContent())
                .entryTitle(journalEntryRequest.getTitle())
                .build());

    journalDao.saveJournal(existingJournal);

    return journalMapper.toJournalResponse(existingJournal);
  }

  private Journal findJournalById(String journalId) {
    Journal existingJournal = journalDao.findById(journalId);
    if (Objects.isNull(existingJournal)) {
      throw new ApiException(String.format("Journal with id: %s not found", journalId), 404);
    }
    return existingJournal;
  }

  public List<JournalEntryResponse> getJournalEntriesByDate(
      String journalId, Instant dateFrom, Instant dateTo) {
    return null;
  }

  public List<JournalResponse> getAllJournals() {
    List<Journal> allJournals = journalDao.findAll();
    return allJournals.stream().map(journalMapper::toJournalResponse).toList();
  }

  public void deleteJournalById(String journalId) {
    findJournalById(journalId);
    journalDao.deleteById(journalId);
  }
}

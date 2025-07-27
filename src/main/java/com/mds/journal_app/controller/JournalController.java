package com.mds.journal_app.controller;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.exceptions.AppException;
import com.mds.journal_app.pojo.*;
import com.mds.journal_app.service.JournalService;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class JournalController {

  private final JournalService journalService;

  /*
   * creates or updates a journal
   * if journal with same title exists, it will update the existing journal, otherwise it will create a new journal
   * */
  @PostMapping("/journal")
  public ResponseEntity<PostJournalResponse> postJournal(
      @RequestBody @Valid JournalRequest journalRequest) throws AppException {
    return ResponseEntity.ok().body(journalService.postJournal(journalRequest));
  }

  /** create new entry in a journal */
  @PostMapping("journal/{journalId}/entry")
  public ResponseEntity<String> createJournalEntry(
      @PathVariable String journalId, @RequestBody JournalEntryRequest journalEntryRequest) {
    log.info("createJournalEntry() initiated");
    journalService.postJournalEntry(journalId, journalEntryRequest);
    return ResponseEntity.ok().body("journal entry created successfully!");
  }

  /** get all the entries for a journal between a date range */
  @GetMapping("journal/{journalId}/entry")
  public ResponseEntity<List<JournalEntryResponse>> getJournalEntriesByDate(
      @PathVariable String journalId,
      @RequestParam Instant dateFrom,
      @RequestParam Instant dateTo) {
    log.info("getJournalEntriesByDate() initiated");
    return ResponseEntity.ok()
        .body(journalService.getJournalEntriesByDate(journalId, dateFrom, dateTo));
  }

  @GetMapping("journal")
  public ResponseEntity<List<JournalResponse>> getAllJournals() {
    log.info("getAllJournals() initiated");
    return ResponseEntity.ok().body(journalService.getAllJournals());
  }

  @GetMapping("journal/{id}")
  public ResponseEntity<JournalResponse> getJournalById(@PathVariable String id) {
    log.info("getJournalById() initiated");
    return ResponseEntity.ok().body(journalService.getJournalById(id));
  }

  @DeleteMapping("journal/{journalId}")
  public ResponseEntity<List<Journal>> deleteJournalById(@PathVariable String journalId) {
    log.info("delete journal by id:{} initiated", journalId);
    journalService.deleteJournalById(journalId);
    return ResponseEntity.ok().body(null);
  }
}

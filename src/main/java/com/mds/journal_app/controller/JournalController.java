package com.mds.journal_app.controller;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.pojo.*;
import com.mds.journal_app.service.JournalService;
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

  @GetMapping("/test")
  public ResponseEntity<String> testGet() {
    log.info("testGet() initiated");
    return ResponseEntity.ok().body(journalService.testGet());
  }

  /** create new journal */
  @PostMapping("/journal")
  public ResponseEntity<String> createJournal(@RequestBody JournalRequest journalRequest) {
    log.info("createUpdateJournal() initiated");
    journalService.postJournal(journalRequest);
    return ResponseEntity.ok().body("journal created successfully!");
  }

  /** create new entry in a journal */
  @PostMapping("journal/{journalId}/entry")
  public ResponseEntity<String> createJournalEntry(
      @PathVariable String journalId, @RequestBody JournalEntryRequest journalEntryRequest)
      throws JournalNotFoundException {
    log.info("createJournalEntry() initiated");
    journalService.postJournalEntry(journalId, journalEntryRequest);
    return ResponseEntity.ok().body("journal entry created successfully!");
  }

  /** get all the entries for a journal between a date range */
  @GetMapping("journal/{journalId}/entry")
  public ResponseEntity<List<JournalEntryResponse>> getJournalEntriesByDate(
      @PathVariable String journalId, @RequestParam Instant dateFrom, @RequestParam Instant dateTo)
      throws JournalNotFoundException {
    log.info("getJournalEntriesByDate() initiated");
    return ResponseEntity.ok()
        .body(journalService.getJournalEntriesByDate(journalId, dateFrom, dateTo));
  }

  @GetMapping("journal")
  public ResponseEntity<List<JournalResponse>> getAllJournals() {
    log.info("getAllJournals() initiated");
    return ResponseEntity.ok().body(journalService.getAllJournals());
  }

  @DeleteMapping("journal/{journalId}")
  public ResponseEntity<List<Journal>> deleteJournalById(@PathVariable String journalId)
      throws JournalNotFoundException {
    log.info("delete journal by id:{} initiated", journalId);
    journalService.deleteJournalById(journalId);
    return ResponseEntity.ok().body(null);
  }
}

package com.mds.journal_app.controller;

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
  public ResponseEntity<BaseApiResponse<List<JournalResponse>>> getAllJournals() {
    log.info("getAllJournals() initiated");
    return ResponseEntity.ok()
        .body(
            BaseApiResponse.success(
                200, "fetched all journals successfully", journalService.getAllJournals()));
  }

  @DeleteMapping("journal/{journalId}")
  public ResponseEntity<BaseApiResponse<String>> deleteJournalById(@PathVariable String journalId) {
    log.info("delete journal by id:{} initiated", journalId);
    journalService.deleteJournalById(journalId);
    return ResponseEntity.ok()
        .body(BaseApiResponse.success(200, "journal deleted successfully", journalId));
  }

  /** create new journal */
  @PostMapping("/journal")
  public ResponseEntity<BaseApiResponse<JournalResponse>> createJournal(
      @RequestBody JournalRequest journalRequest) {
    log.info("createUpdateJournal() initiated");
    return ResponseEntity.ok()
        .body(
            BaseApiResponse.success(
                200, "journal created successfully", journalService.postJournal(journalRequest)));
  }

  /** create new entry in a journal */
  @PostMapping("journal/{journalId}/entry")
  public ResponseEntity<BaseApiResponse<JournalResponse>> createJournalEntry(
      @PathVariable String journalId, @RequestBody JournalEntryRequest journalEntryRequest) {
    log.info("createJournalEntry() initiated");
    return ResponseEntity.ok()
        .body(
            BaseApiResponse.success(
                200,
                "journal entry created successfully",
                journalService.postJournalEntry(journalId, journalEntryRequest)));
  }

  @GetMapping("/health")
  public ResponseEntity<BaseApiResponse<String>> healthCheck() {
    log.info("healhCheck() initiated");
    return ResponseEntity.ok()
        .body(BaseApiResponse.success(200, "API is healthy", "Hello from Journal API"));
  }
}

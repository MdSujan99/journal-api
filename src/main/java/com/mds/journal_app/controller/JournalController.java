package com.mds.journal_app.controller;

import com.mds.journal_app.pojo.*;
import com.mds.journal_app.service.JournalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Journal", description = "Endpoints for managing journals and entries")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/")
public class JournalController {
  private final JournalService journalService;

  @Operation(summary = "Get all journals", description = "Fetches all journals")
  @GetMapping("journal")
  public ResponseEntity<BaseApiResponse<List<JournalResponse>>> getAllJournals() {
    log.info("getAllJournals() initiated");
    return ResponseEntity.ok()
        .body(
            BaseApiResponse.success(
                200, "fetched all journals successfully", journalService.getAllJournals()));
  }

  /** get all the entries for a journal between a date range */
  @Operation(
      summary = "Get journal entries by date",
      description = "Fetches all entries for a journal within the specified date range")
  @GetMapping("journal/{journalId}/entry")
  public ResponseEntity<List<JournalEntryResponse>> getJournalEntriesByDate(
      @PathVariable String journalId,
      @RequestParam Instant dateFrom,
      @RequestParam Instant dateTo) {
    log.info("getJournalEntriesByDate() initiated");
    return ResponseEntity.ok()
        .body(journalService.getJournalEntriesByDate(journalId, dateFrom, dateTo));
  }

  @Operation(summary = "Delete journal by ID", description = "Deletes a journal by its ID")
  @DeleteMapping("journal/{journalId}")
  public ResponseEntity<BaseApiResponse<String>> deleteJournalById(@PathVariable String journalId) {
    log.info("delete journal by id:{} initiated", journalId);
    journalService.deleteJournalById(journalId);
    return ResponseEntity.ok()
        .body(BaseApiResponse.success(200, "journal deleted successfully", journalId));
  }

  /** create new journal */
  @Operation(summary = "Create journal", description = "Creates a new journal")
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
  @Operation(summary = "Create journal entry", description = "Creates a new entry in a journal")
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

  @Operation(summary = "Health check", description = "Checks API health status")
  @GetMapping("/health")
  public ResponseEntity<BaseApiResponse<String>> healthCheck() {
    log.info("healhCheck() initiated");
    return ResponseEntity.ok()
        .body(BaseApiResponse.success(200, "API is healthy", "Hello from Journal API"));
  }
}

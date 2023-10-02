package com.mds.journal_app.controller;

import com.mds.journal_app.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.mds.journal_app.service.JournalService;

import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/api/")
@Slf4j
public class JournalController {
    @Autowired
    private JournalService journalService;

    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        log.info("testGet() initiated");
        return ResponseEntity.ok().body(journalService.testGet());
    }

    /**
     * create new journal
     */
    @PostMapping("/journal")
    public ResponseEntity<PostJournalResponse> createUpdateJournal(
            @RequestBody PostJournalRequest postJournalRequest) {
        log.info("createUpdateJournal() initiated");
        return ResponseEntity.ok().body(journalService.postJournal(postJournalRequest));
    }

    /**
     * create new entry in a journal
     */
    @PostMapping("journal/{journalId}/entry")
    public ResponseEntity<PostJournalEntryResponse> createJournalEntry(
            @RequestBody PostJournalEntryRequest postJournalEntryRequest) {
        log.info("createJournalEntry() initiated");
        return ResponseEntity.ok().body(journalService.postJournalEntry(postJournalEntryRequest));
    }

    /**
     * get all the entries for a journal between a date range
     */
    @GetMapping("journal/{journalId}/entry")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByDate(
            @RequestParam Instant dateFrom,
            @RequestParam Instant dateTo) {
        log.info("getJournalEntriesByDate() initiated");
        return ResponseEntity.ok().body(journalService.getJournalsByDate(dateFrom, dateTo));
    }
    @GetMapping("journal")
    public ResponseEntity<List<JournalEntry>> getAllJournals() {
        log.info("getAllJournals() initiated");
        return ResponseEntity.ok().body(journalService.getAllJournals());
    }
}

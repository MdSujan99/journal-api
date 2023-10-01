package com.mds.journal_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mds.journal_app.service.JournalService;

@Controller
@RequestMapping("/")
public class JournalController {
    @Autowired private JournalService journalService;
    @GetMapping("/test")
    public ResponseEntity<String> testGet(){
        return ResponseEntity.ok().body(journalService.testGet());
    }
}

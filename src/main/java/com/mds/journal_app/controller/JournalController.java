package com.mds.journal_app.controller;

import com.mds.journal_app.pojo.PostJournalRequest;
import com.mds.journal_app.pojo.PostJournalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mds.journal_app.service.JournalService;

@Controller
@RequestMapping("/api/")
public class JournalController {
    @Autowired
    private JournalService journalService;

    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.ok().body(journalService.testGet());
    }

    /*
    1. **Create or Update a Journal for a User**:
       - Request Body: Journal details (e.g., title, description)
       - Response: The created journal object with an ID
    */
    @PostMapping("/users/{userId}/journal")
    public ResponseEntity<PostJournalResponse> createUpdateJournal(@RequestBody PostJournalRequest postJournalRequest) {
        return ResponseEntity.ok().body(
                journalService.postJournal(postJournalRequest));
    }
    /*
    2. **Update an Existing Journal Entry**:
       - Method: PUT
       - Endpoint: `/api/journals/{journalId}/entries/{entryId}`
       - Request Body: Updated journal entry details
       - Response: Updated journal entry object
    */
    /*
    3. **View Old Journal Entries by Date and Journal Name**:
       - Method: GET
       - Endpoint: `/api/journals/{journalName}/entries`
       - Query Parameters:
         - `date` (e.g., `2023-09-15`) - The date for which entries are to be retrieved
       - Response: List of journal entries for the specified date and journal name
    */
    /*
    4. **Download One or More Journal Entries in JSON Format**:
       - Method: GET
       - Endpoint: `/api/journals/{journalId}/download`
       - Query Parameters (Optional):
         - `entryIds` (e.g., `1,2,3`) - A comma-separated list of entry IDs to download
       - Response: JSON representation of the selected journal entries
    */
}

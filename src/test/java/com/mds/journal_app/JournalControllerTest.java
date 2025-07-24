package com.mds.journal_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.mds.journal_app.controller.JournalController;
import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.pojo.*;
import com.mds.journal_app.service.JournalService;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class JournalControllerTest {
  @Mock private JournalService journalService;

  @InjectMocks private JournalController journalController;

  @Test
  void createJournal_ShouldReturnSuccess() {
    // Arrange
    JournalRequest request = JournalRequest.builder().build();
    request.setTitle("Test Journal");
    doNothing().when(journalService).postJournal(any(JournalRequest.class));

    // Act
    ResponseEntity<String> response = journalController.createJournal(request);

    // Assert
    assertEquals("journal created successfully!", response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(journalService).postJournal(request);
  }

  @Test
  void createJournalEntry_ShouldReturnSuccess() throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    JournalEntryRequest request = new JournalEntryRequest();
    request.setTextContent("Test Entry");
    doNothing()
        .when(journalService)
        .postJournalEntry(eq(journalId), any(JournalEntryRequest.class));

    // Act
    ResponseEntity<String> response = journalController.createJournalEntry(journalId, request);

    // Assert
    assertEquals("journal entry created successfully!", response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(journalService).postJournalEntry(journalId, request);
  }

  @Test
  void createJournalEntry_WhenJournalNotFound_ShouldThrowException()
      throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    JournalEntryRequest request = new JournalEntryRequest();
    doThrow(new JournalNotFoundException())
        .when(journalService)
        .postJournalEntry(eq(journalId), any(JournalEntryRequest.class));

    // Act & Assert
    assertThrows(
        JournalNotFoundException.class,
        () -> journalController.createJournalEntry(journalId, request));
    verify(journalService).postJournalEntry(journalId, request);
  }

  @Test
  void getJournalEntriesByDate_ShouldReturnEntries() throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    Instant dateFrom = Instant.parse("2023-01-01T00:00:00Z");
    Instant dateTo = Instant.parse("2023-12-31T23:59:59Z");
    List<JournalEntryResponse> expectedEntries =
        Arrays.asList(
            JournalEntryResponse.builder().textContent("Entry 1").build(),
            JournalEntryResponse.builder().textContent("Entry 2").build());
    when(journalService.getJournalEntriesByDate(journalId, dateFrom, dateTo))
        .thenReturn(expectedEntries);

    // Act
    ResponseEntity<List<JournalEntryResponse>> response =
        journalController.getJournalEntriesByDate(journalId, dateFrom, dateTo);

    // Assert
    assertEquals(expectedEntries, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(journalService).getJournalEntriesByDate(journalId, dateFrom, dateTo);
  }

  @Test
  void getJournalEntriesByDate_WhenJournalNotFound_ShouldThrowException()
      throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    Instant dateFrom = Instant.parse("2023-01-01T00:00:00Z");
    Instant dateTo = Instant.parse("2023-12-31T23:59:59Z");
    when(journalService.getJournalEntriesByDate(journalId, dateFrom, dateTo))
        .thenThrow(new JournalNotFoundException());

    // Act & Assert
    assertThrows(
        JournalNotFoundException.class,
        () -> journalController.getJournalEntriesByDate(journalId, dateFrom, dateTo));
    verify(journalService).getJournalEntriesByDate(journalId, dateFrom, dateTo);
  }

  @Test
  void getAllJournals_ShouldReturnJournalsList() {
    // Arrange
    List<JournalResponse> expectedJournals =
        Arrays.asList(
            JournalResponse.builder().title("Journal 1").build(),
            JournalResponse.builder().title("Journal 2").build());
    when(journalService.getAllJournals()).thenReturn(expectedJournals);

    // Act
    ResponseEntity<List<JournalResponse>> response = journalController.getAllJournals();

    // Assert
    assertEquals(expectedJournals, response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(journalService).getAllJournals();
  }

  @Test
  void deleteJournalById_ShouldReturnSuccess() throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    doNothing().when(journalService).deleteJournalById(journalId);

    // Act
    ResponseEntity<List<Journal>> response = journalController.deleteJournalById(journalId);

    // Assert
    assertNull(response.getBody());
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    verify(journalService).deleteJournalById(journalId);
  }

  @Test
  void deleteJournalById_WhenJournalNotFound_ShouldThrowException()
      throws JournalNotFoundException {
    // Arrange
    String journalId = "123";
    doThrow(new JournalNotFoundException()).when(journalService).deleteJournalById(journalId);

    // Act & Assert
    assertThrows(
        JournalNotFoundException.class, () -> journalController.deleteJournalById(journalId));
    verify(journalService).deleteJournalById(journalId);
  }
}

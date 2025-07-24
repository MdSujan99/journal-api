package com.mds.journal_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.dao.JournalRepo;
import com.mds.journal_app.exceptions.JournalNotFoundException;
import com.mds.journal_app.pojo.JournalEntryRequest;
import com.mds.journal_app.pojo.JournalRequest;
import com.mds.journal_app.service.JournalService;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JournalServiceTest {
  @Mock private JournalRepo journalRepo;

  @InjectMocks private JournalService journalService;

  @Test
  void postJournal_ShouldCreateJournal_WhenValidRequest() {
    // Arrange
    JournalRequest request =
        JournalRequest.builder().title("Test Journal").description("Test Description").build();

    // Act
    journalService.postJournal(request);

    // Assert
    verify(journalRepo, times(1)).save(any(Journal.class));
  }

  @Test
  void postJournal_ShouldHandleNullDescription() {
    // Arrange
    JournalRequest request = JournalRequest.builder().title("Test Journal").build();

    // Act
    journalService.postJournal(request);

    // Assert
    verify(journalRepo, times(1)).save(any(Journal.class));
  }

  @Test
  void postJournalEntry_ShouldAddEntry_WhenJournalExists() throws JournalNotFoundException {
    // Arrange
    String journalId = "test-id";
    JournalEntryRequest entryRequest =
        JournalEntryRequest.builder().textContent("Test Entry Content").build();

    Journal existingJournal =
        Journal.builder().id(journalId).journalEntryMap(new HashMap<>()).build();

    when(journalRepo.findById(journalId)).thenReturn(Optional.of(existingJournal));

    // Act
    journalService.postJournalEntry(journalId, entryRequest);

    // Assert
    verify(journalRepo, times(1)).findById(journalId);
  }

  @Test
  void postJournalEntry_ShouldThrowException_WhenJournalNotFound() {
    // Arrange
    String journalId = "non-existent-id";
    JournalEntryRequest entryRequest =
        JournalEntryRequest.builder().textContent("Test Entry Content").build();

    when(journalRepo.findById(journalId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(
        JournalNotFoundException.class,
        () -> journalService.postJournalEntry(journalId, entryRequest));
  }

  @Test
  void postJournalEntry_ShouldInitializeEntryMap_WhenMapIsNull() throws JournalNotFoundException {
    // Arrange
    String journalId = "test-id";
    JournalEntryRequest entryRequest =
        JournalEntryRequest.builder().textContent("Test Entry Content").build();

    Journal existingJournal = Journal.builder().id(journalId).journalEntryMap(null).build();

    when(journalRepo.findById(journalId)).thenReturn(Optional.of(existingJournal));

    // Act
    journalService.postJournalEntry(journalId, entryRequest);

    // Assert
    verify(journalRepo, times(1)).findById(journalId);
    assertNotNull(existingJournal.getJournalEntryMap());
  }

  @Test
  void postJournal_ShouldHandleEmptyTitle() {
    // Arrange
    JournalRequest request =
        JournalRequest.builder().title("").description("Test Description").build();

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> journalService.postJournal(request));
  }

  @Test
  void postJournal_ShouldHandleNullTitle() {
    // Arrange
    JournalRequest request = JournalRequest.builder().description("Test Description").build();

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> journalService.postJournal(request));
  }

  @Test
  void postJournalEntry_ShouldHandleEmptyContent() {
    // Arrange
    String journalId = "test-id";
    JournalEntryRequest entryRequest = JournalEntryRequest.builder().textContent("").build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> journalService.postJournalEntry(journalId, entryRequest));
  }

  @Test
  void postJournalEntry_ShouldHandleNullContent() {
    // Arrange
    String journalId = "test-id";
    JournalEntryRequest entryRequest = JournalEntryRequest.builder().build();

    // Act & Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> journalService.postJournalEntry(journalId, entryRequest));
  }

  @Test
  void postJournalEntry_ShouldHandleMaxContentLength() throws JournalNotFoundException {
    // Arrange
    String journalId = "test-id";
    String longContent = "a".repeat(1000); // Assuming max length is more than 1000
    JournalEntryRequest entryRequest =
        JournalEntryRequest.builder().textContent(longContent).build();

    Journal existingJournal =
        Journal.builder().id(journalId).journalEntryMap(new HashMap<>()).build();

    when(journalRepo.findById(journalId)).thenReturn(Optional.of(existingJournal));

    // Act
    journalService.postJournalEntry(journalId, entryRequest);

    // Assert
    verify(journalRepo).save(existingJournal);
  }

  @Test
  void postJournalEntry_ShouldGenerateUniqueEntryId() throws JournalNotFoundException {
    // Arrange
    String journalId = "test-id";
    JournalEntryRequest entryRequest =
        JournalEntryRequest.builder().textContent("Test Content").build();

    Journal existingJournal =
        Journal.builder().id(journalId).journalEntryMap(new HashMap<>()).build();

    when(journalRepo.findById(journalId)).thenReturn(Optional.of(existingJournal));

    // Act
    journalService.postJournalEntry(journalId, entryRequest);
    journalService.postJournalEntry(journalId, entryRequest);

    // Assert
    assertEquals(2, existingJournal.getJournalEntryMap().size());
    assertNotEquals(
        existingJournal.getJournalEntryMap().keySet().toArray()[0],
        existingJournal.getJournalEntryMap().keySet().toArray()[1]);
  }

  @Test
  void postJournal_ShouldSetCreationTimestamp() {
    // Arrange
    JournalRequest request =
        JournalRequest.builder().title("Test Journal").description("Test Description").build();

    // Act
    journalService.postJournal(request);

    // Assert
    verify(journalRepo).save(argThat(journal -> journal.getCreatedAt() != null));
  }
}

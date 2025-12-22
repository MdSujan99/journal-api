package com.mds.journal_app;

import static com.mds.journal_app.TestUtils.JOURNAL_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.journal_app.controller.JournalController;
import com.mds.journal_app.pojo.JournalRequest;
import com.mds.journal_app.pojo.JournalResponse;
import com.mds.journal_app.service.JournalService;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ActiveProfiles("test")
@WebMvcTest(JournalController.class)
class JournalControllerTest {
  @MockBean private JournalService journalService;

  @Autowired private MockMvc mockMvc;
  private final TestUtils testUtils = new TestUtils();
  @Autowired private ObjectMapper objectMapper;

  @Test
  void test_getAllJournals_whenNoJournals() throws Exception {

    when(journalService.getAllJournals()).thenReturn(Collections.emptyList());

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/journal")
                .accept(org.springframework.http.MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload", Matchers.hasSize(0)));
  }

  @Test
  void test_getAllJournals_hasJournals() throws Exception {

    List<JournalResponse> sampleJournals = testUtils.getSampleJournals();
    when(journalService.getAllJournals()).thenReturn(sampleJournals);

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/journal")
                .accept(org.springframework.http.MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.payload", Matchers.hasSize(sampleJournals.size())));
  }

  @Test
  void test_deleteJournalsById() throws Exception {
    doNothing().when(journalService).deleteJournalById(any());
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/journal/" + JOURNAL_ID)
                .accept(org.springframework.http.MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload", Matchers.equalTo(JOURNAL_ID)));
  }

  @Test
  void test_createJournal() throws Exception {

    JournalRequest request = testUtils.getSampleJournalRequest();
    JournalResponse response = testUtils.getSampleJournalResponse();
    when(journalService.postJournal(any())).thenReturn(response);

    mockMvc
        .perform(
            post("/api/journal")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.payload.id", Matchers.equalTo(response.getId())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.payload.title", Matchers.equalTo(response.getTitle())))
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.payload.description", Matchers.equalTo(response.getDescription())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload.createdAt").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload.updatedAt").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload.journalEntryMap").isEmpty());
  }
}

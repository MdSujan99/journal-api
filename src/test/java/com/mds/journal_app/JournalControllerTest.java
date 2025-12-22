package com.mds.journal_app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.mds.journal_app.controller.JournalController;
import com.mds.journal_app.pojo.JournalResponse;
import com.mds.journal_app.service.JournalService;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class JournalControllerTest {
  @Mock private JournalService journalService;

  @InjectMocks private JournalController journalController;
  @InjectMocks private TestUtils testUtils;

  @Test
  void test_getAllJournals_whenNoJournals() throws Exception {

    when(journalService.getAllJournals()).thenReturn(Collections.emptyList());
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(journalController).build();

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
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(journalController).build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/journal")
                .accept(org.springframework.http.MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.payload", Matchers.hasSize(sampleJournals.size())));
  }
}

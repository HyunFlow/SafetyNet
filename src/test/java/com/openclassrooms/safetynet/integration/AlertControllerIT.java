package com.openclassrooms.safetynet.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AlertControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getChildAlertByAddress_shouldReturnChildAlertDto() throws Exception {
    mockMvc.perform(get("/childAlert")
            .param("address", "1509 Culver St"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.children").isArray())
        .andExpect(jsonPath("$.family").isArray());
  }

  @Test
  void phoneAlert_shouldReturnPhoneAlertDto() throws Exception {
    mockMvc.perform(get("/phoneAlert")
        .param("stationNumber", String.valueOf(2)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.phoneNumber").isArray());
  }

}

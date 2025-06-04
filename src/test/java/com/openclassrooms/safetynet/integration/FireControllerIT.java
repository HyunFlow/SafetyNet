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
public class FireControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getFireInfoByAddress_shouldReturnFireDto() throws Exception {
    mockMvc.perform(get("/fire")
        .param("address", "1509 Culver St"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stationNumber").isNumber())
        .andExpect(jsonPath("$.residents").isArray());
  }
}

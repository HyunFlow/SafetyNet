package com.openclassrooms.safetynet.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class FirestationControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getListOfPeopleByStationNumber_shouldReturnFirestationDto() throws Exception {
    mockMvc.perform(get("/firestation")
            .param("stationNumber", String.valueOf(2)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.persons").isArray())
        .andExpect(jsonPath("$.adultCount").isNumber())
        .andExpect(jsonPath("$.childCount").isNumber());
  }

  @Test
  void postFirestationMapping_shouldReturnResponseCreated() throws Exception {
    mockMvc.perform(post("/firestation")
        .param("address", "555 Robert St")
        .param("stationNumber", String.valueOf(2)))
        .andExpect(status().isCreated());
  }

  @Test
  void updateFirestation_shouldReturnResponseOk() throws Exception {
    mockMvc.perform(put("/firestation")
        .param("address", "1509 Culver St")
        .param("stationNumber", String.valueOf(99)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteFirestationByAddress_shouldReturnResponseOk() throws Exception {
    mockMvc.perform(delete("/firestation")
        .param("address", "1509 Culver St"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteFirestationByStationNumber_shouldReturnResponseOk() throws Exception {
    mockMvc.perform(delete("/firestation")
            .param("stationNumber", String.valueOf(2)))
        .andExpect(status().isOk());
  }
}

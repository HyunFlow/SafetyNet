package com.openclassrooms.safetynet.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.safetynet.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.service.FirestationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FirestationController.class)
@AutoConfigureMockMvc
public class FirestationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FirestationService firestationService;

  @Test
  void getListOfPeopleByStation_shouldReturnFirestationDto() throws Exception {
    // given
    int stationNumber = 1;
    FirestationResponseDTO dto = new FirestationResponseDTO();
    dto.setPersons(List.of(
        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
            "jaboyd@email.com", 41),
        new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513",
            "drk@email.com", 36)
    ));
    dto.setAdultCount(2);
    dto.setChildCount(0);

    // when
    when(firestationService.getPeopleByStation(stationNumber)).thenReturn(dto);

    // then
    mockMvc.perform(get("/firestation")
            .param("stationNumber", "1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.persons").isArray())
        .andExpect(jsonPath("$.persons.length()").value(2))
        .andExpect(jsonPath("$.adultCount").value(2))
        .andExpect(jsonPath("$.childCount").value(0));
  }

  @Test
  void postFirestationMapping_shouldReturnResponseCreated_whenSuccessfullyAdded() throws Exception {
    // given
    String inputAddress = "1509 Culver St";
    int stationNumber = 1;

    // when
    when(firestationService.addFirestation(inputAddress, stationNumber)).thenReturn(true);

    // then
    mockMvc.perform(post("/firestation")
            .param("address", inputAddress)
            .param("stationNumber", String.valueOf(stationNumber))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().string("Successfully added firestation"));
  }

  @Test
  void postFirestationMapping_shouldReturnResponseConflict_whenMappingExist() throws Exception {
    // given
    String inputAddress = "1509 Culver St";
    int stationNumber = 1;

    // when
    when(firestationService.addFirestation(inputAddress, stationNumber)).thenReturn(false);

    // then
    mockMvc.perform(post("/firestation")
            .param("address", inputAddress)
            .param("stationNumber", String.valueOf(stationNumber))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(content().string("Mapping already exists"));
  }

  @Test
  void updateFirestation_shouldReturnResponseOk_whenSuccessfullyUpdated() throws Exception {
    // given
    String inputAddress = "1509 Culver St";
    int stationNumber = 2;

    // when
    when(firestationService.setFirestation(inputAddress, stationNumber)).thenReturn(true);

    // then
    mockMvc.perform(put("/firestation")
            .param("address", inputAddress)
            .param("stationNumber", String.valueOf(stationNumber))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Successfully updated firestation"));
  }

  @Test
  void updateFirestation_shouldReturnNotFound_whenAddressCannotFind() throws Exception {
    // given
    String inputAddress = "UnKnown Address";
    int stationNumber = 99;

    // when
    when(firestationService.setFirestation(inputAddress, stationNumber)).thenReturn(false);

    // then
    mockMvc.perform(put("/firestation")
            .param("address", inputAddress)
            .param("stationNumber", String.valueOf(stationNumber))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Firestation address not found"));
  }

  @Test
  void deleteFirestationByAddress_shouldReturnResponseOk_whenSuccessfullyDeleted()
      throws Exception {
    // given
    String inputAddress = "1509 Culver St";

    // when
    when(firestationService.deleteFirestationByAddress(inputAddress)).thenReturn(true);

    // then
    mockMvc.perform(delete("/firestation")
            .param("address", inputAddress)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Deleted firestation with address: " + inputAddress));
  }

  @Test
  void deleteFirestationByAddress_shouldReturnNotFound_whenAddressDoesNotExist() throws Exception {
    // given
    String inputAddress = "Unknown address";

    // when
    when(firestationService.deleteFirestationByAddress(inputAddress)).thenReturn(false);

    // then
    mockMvc.perform(delete("/firestation").param("address", inputAddress))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Firestation address not found"));
  }

  @Test
  void deleteFirestationByStation_shouldReturnResponseOk_whenSuccessfullyDeleted()
      throws Exception {
    // given
    int inputStationNumber = 2;

    // when
    when(firestationService.deleteFirestationByStation(inputStationNumber)).thenReturn(true);

    // then
    mockMvc.perform(delete("/firestation")
            .param("stationNumber", String.valueOf(inputStationNumber))
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            content().string("Deleted firestation with station number: " + inputStationNumber));
  }

  @Test
  void deleteFirestationByStation_shouldReturnNotFound_whenStationDoesNotExist()
      throws Exception {
    // given
    int inputStationNumber = 99;

    // when
    when(firestationService.deleteFirestationByStation(inputStationNumber)).thenReturn(false);

    // then
    mockMvc.perform(delete("/firestation")
            .param("stationNumber", String.valueOf(inputStationNumber)))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Firestation with this station number not found"));
  }
}

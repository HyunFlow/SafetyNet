package com.openclassrooms.safetynet.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.dto.ResidentDTO;
import com.openclassrooms.safetynet.service.FireService;
import com.openclassrooms.safetynet.service.FirestationService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FireController.class)
public class FireControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FireService fireService;

  @MockitoBean
  private FirestationService firestationService;

  @Test
  void getFireInfoByAddress_shouldReturnFireDto() throws Exception {
    // given
    String inputAddress = "1509 Culver St";
    int stationNumber = 2;

    List<ResidentDTO> residentDtos = List.of(
        new ResidentDTO("John", "Boyd", null, 41, null, null),
        new ResidentDTO("Jacob", "Boyd", null, 36, null, null)
    );

    FireResponseDTO dto = new FireResponseDTO(stationNumber, residentDtos);

    // when
    when(firestationService.getStationNumberByAddress(inputAddress)).thenReturn(stationNumber);
    when(fireService.findResidentsByAddress(inputAddress)).thenReturn(dto);

    // then
    mockMvc.perform(get("/fire")
        .param("address", inputAddress)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.stationNumber").value(2))
        .andExpect(jsonPath("$.residents").isArray())
        .andExpect(jsonPath("$.residents.length()").value(2))
        .andExpect(jsonPath("$.residents[0].firstName").value("John"))
        .andExpect(jsonPath("$.residents[0].lastName").value("Boyd"))
        .andExpect(jsonPath("$.residents[1].firstName").value("Jacob"))
        .andExpect(jsonPath("$.residents[1].lastName").value("Boyd"));
  }

  @Test
  void getFireInfoByAddress_whenNoFirestationFound_shouldReturnEmptyResidentList() throws Exception {
    // given
    String inputAddress = "empty";

    List<ResidentDTO> residentDtos = List.of();
    FireResponseDTO dto = new FireResponseDTO(-1, residentDtos);

    // when
    when(firestationService.getStationNumberByAddress(inputAddress)).thenReturn(-1);
    when(fireService.findResidentsByAddress(inputAddress)).thenReturn(dto);

    // then
    mockMvc.perform(get("/fire")
            .param("address", inputAddress)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.stationNumber").value(-1))
        .andExpect(jsonPath("$.residents").isArray())
        .andExpect(jsonPath("$.residents.length()").value(0));
  }
}

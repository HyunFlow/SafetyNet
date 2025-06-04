package com.openclassrooms.safetynet.controller;

import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.service.MedicalRecordService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MedicalRecordService medicalRecordService;

  @Test
  void getMedicalRecordByName_shouldReturnMedicalRecordDto() throws Exception {
    String firstName = "John";
    String lastName = "Boyd";

    List<MedicalRecordResponseDTO> recordDto = List.of(
        new MedicalRecordResponseDTO("John", "Boyd", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan"))
    );

    Gson gson = new Gson();
    String json = gson.toJson(recordDto);

    // when
    when(medicalRecordService.findMedicalRecordsByName(firstName, lastName)).thenReturn(recordDto);

    // then
    mockMvc.perform(get("/medicalRecord")
        .param("firstName", firstName)
        .param("lastName", lastName)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Boyd"))
        .andExpect(jsonPath("$[0].medications", contains("aznol:350mg","hydrapermazol:100mg")))
        .andExpect(jsonPath("$[0].allergies", contains("nillacilan")));
  }

  @Test
  void createMedicalRecord_shouldReturnResponseCreated() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "John", "Boyd", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.createNewMedicalRecord(requestDto)).thenReturn(true);

    // then
    mockMvc.perform(post("/medicalRecord")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(content().string("New medical record created"));
  }

  @Test
  void createMedicalRecord_shouldReturnResponseConflict() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "John", "Boyd", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.createNewMedicalRecord(requestDto)).thenReturn(false);

    // then
    mockMvc.perform(post("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Create a Medical record failed"));
  }

  @Test
  void updateMedicalRecord_shouldReturnResponseOk() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "John", "Boyd", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.updateMedicalRecord(requestDto)).thenReturn(true);

    // then
    mockMvc.perform(put("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("Medical record updated"));
  }

  @Test
  void updateMedicalRecord_shouldReturnConflict() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "Unknown", "Unknown", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.updateMedicalRecord(requestDto)).thenReturn(false);

    // then
    mockMvc.perform(put("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Update a Medical record failed"));
  }

  @Test
  void deleteMedicalRecord_shouldReturnResponseOk() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "John", "Boyd", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.deleteMedicalRecord(requestDto)).thenReturn(true);

    // then
    mockMvc.perform(delete("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("Medical record deleted"));
  }

  @Test
  void deleteMedicalRecord_shouldReturnConflict() throws Exception {
    // given
    MedicalRecordResponseDTO requestDto = new MedicalRecordResponseDTO(
        "Unknown", "Unknown", "03/06/1984", List.of("aznol:350mg","hydrapermazol:100mg"), List.of("nillacilan")
    );

    Gson gson = new Gson();
    String json = gson.toJson(requestDto);

    // when
    when(medicalRecordService.deleteMedicalRecord(requestDto)).thenReturn(false);

    // then
    mockMvc.perform(delete("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Delete a Medical record failed"));
  }

}

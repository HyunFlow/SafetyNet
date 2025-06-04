package com.openclassrooms.safetynet.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MedicalRecordControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getMedicalRecordByName_shouldReturnListOfMedicalRecordsDto() throws Exception {
    mockMvc.perform(get("/medicalRecord")
            .param("firstName", "John")
            .param("lastName", "Boyd")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].length()").value(5))
        .andExpect(jsonPath("$[0].firstName").value("John"))
        .andExpect(jsonPath("$[0].lastName").value("Boyd"))
        .andExpect(jsonPath("$[0].medications").isArray())
        .andExpect(jsonPath("$[0].allergies").isArray());
  }

  @Test
  void createMedicalRecord_shouldReturnResponseCreated() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("Patrick");
    dto.setLastName("Anderson");
    dto.setBirthdate("01/01/2000");
    dto.setMedications(List.of("medication1:500mg"));
    dto.setAllergies(List.of("pollen"));

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(post("/medicalRecord")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void createMedicalRecord_shouldReturnResponseConflict() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");
    dto.setBirthdate("01/01/2000");
    dto.setMedications(List.of("medication1:500mg"));
    dto.setAllergies(List.of("pollen"));

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(post("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }

  @Test
  void updateMedicalRecord_shouldReturnResponseOk() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");
    dto.setBirthdate("01/01/2000");
    dto.setMedications(List.of("medication1:500mg"));
    dto.setAllergies(List.of("pollen"));

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(put("/medicalRecord")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk());
  }

  @Test
  void updateMedicalRecord_shouldReturnResponseConflict() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("Unknown");
    dto.setLastName("Unknown");
    dto.setBirthdate("01/01/2000");
    dto.setMedications(List.of("medication1:500mg"));
    dto.setAllergies(List.of("pollen"));

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(put("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }

  @Test
  void deleteMedicalRecord_shouldReturnResponseOk() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(delete("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk());
  }

  @Test
  void deleteMedicalRecord_shouldReturnConflict() throws Exception {
    MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();
    dto.setFirstName("Unknown");
    dto.setLastName("Unknown");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(delete("/medicalRecord")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }
}

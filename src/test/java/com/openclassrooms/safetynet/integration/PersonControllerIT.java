package com.openclassrooms.safetynet.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.dto.PersonDTO;
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
public class PersonControllerIT {
  @Autowired
  private MockMvc mockMvc;

  @Test
  void createNewPerson_shouldReturnResponseCreated() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("New First Name");
    dto.setLastName("New Last Name");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(post("/person")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated());
  }

  @Test
  void createNewPerson_shouldReturnResponseConflict() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }

  @Test
  void updatePerson_shouldReturnResponseOk() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");
    dto.setAge(99);

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(put("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk());
  }

  @Test
  void updatePerson_shouldReturnResponseConflict() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("Unknown");
    dto.setLastName("Unknown");
    dto.setAge(99);

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(put("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }

  @Test
  void deletePerson_shouldReturnResponseOk() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(delete("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk());
  }

  @Test
  void deletePerson_shouldReturnConflict() throws Exception {
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("Unknown");
    dto.setLastName("Unknown");

    Gson gson = new Gson();
    String json = gson.toJson(dto);

    mockMvc.perform(delete("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict());
  }
}

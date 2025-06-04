package com.openclassrooms.safetynet.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.dto.PersonDTO;
import com.openclassrooms.safetynet.service.PersonInfoService;
import com.openclassrooms.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.PathMatcher;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private PersonService personService;
  @MockitoBean
  private PersonInfoService personInfoService;
  @Autowired
  private PathMatcher mvcPathMatcher;

  @Test
  void createNewPerson_shouldReturnResponseCreated() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.saveNewPerson(personDTO)).thenReturn(true);

    // then
    mockMvc.perform(post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isCreated())
        .andExpect(content().string("New person created"));
  }

  @Test
  void createNewPerson_shouldReturnResponseConflict() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.saveNewPerson(personDTO)).thenReturn(false);

    // then
    mockMvc.perform(post("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Create person failed"));
  }

  @Test
  void updatePerson_shouldReturnResponseOk() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.updatePerson(personDTO)).thenReturn(true);

    // then
    mockMvc.perform(put("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("Person updated"));
  }

  @Test
  void updatePerson_shouldReturnResponseConflict() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.updatePerson(personDTO)).thenReturn(false);

    // then
    mockMvc.perform(put("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Update person failed"));
  }

  @Test
  void deletePerson_shouldReturnResponseOk() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.deletePerson(personDTO)).thenReturn(true);

    // then
    mockMvc.perform(delete("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(content().string("Person deleted"));
  }

  @Test
  void deletePerson_shouldReturnResponseConflict() throws Exception {
    // given
    PersonDTO personDTO = new PersonDTO();
    personDTO.setFirstName("John");
    personDTO.setLastName("Boyd");

    Gson gson = new Gson();
    String json = gson.toJson(personDTO);

    // when
    when(personService.deletePerson(personDTO)).thenReturn(false);

    // then
    mockMvc.perform(delete("/person")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isConflict())
        .andExpect(content().string("Delete person failed"));
  }
}

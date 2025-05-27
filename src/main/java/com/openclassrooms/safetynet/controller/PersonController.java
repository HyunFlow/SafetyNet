package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
import com.openclassrooms.safetynet.service.PersonInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {
  private final PersonInfoService personInfoService;

  /**
   * Recherche les informations personnelles des personnes portant un nom de famille donné.
   */
  @GetMapping("/personInfolastName")
  public List<PersonInfoResponseDTO> getPersonInfoByLastName(@RequestParam String lastName) {
    return personInfoService.findPersonsInfoByLastName(lastName);
  }
}

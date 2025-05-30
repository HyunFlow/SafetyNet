package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonDTO;
import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
import com.openclassrooms.safetynet.service.PersonInfoService;
import com.openclassrooms.safetynet.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PersonController {

  private final PersonInfoService personInfoService;
  private final PersonService personService;

  /**
   * Recherche les informations personnelles des personnes portant un nom de famille donné.
   */
  @GetMapping("/personInfolastName")
  public List<PersonInfoResponseDTO> getPersonInfoByLastName(@RequestParam String lastName) {
    return personInfoService.findPersonsInfoByLastName(lastName);
  }

  /**
   * Recherche toutes les personnes enregistrées dans la base de données.
   */
  @GetMapping("/persons")
  public List<PersonDTO> getAllPersons() {
    return personService.findAllPersons();
  }

  /**
   * Ajoute une nouvelle personne via un corps de requête JSON.
   * Le prénom et le nom de famille sont obligatoires.
   */
  @PostMapping("/person")
  public ResponseEntity<String> postPerson(@RequestBody @Valid PersonDTO personDTO) {
    boolean saved = personService.saveNewPerson(personDTO);
    if (saved) {
      personService.saveNewPerson(personDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body("New person created");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Creat person failed");
    }

  }

  /**
   * Met à jour les informations d'une personne existante dans la base de données via un corps JSON.
   */
  @PutMapping("/person")
  public ResponseEntity<String> putPerson(@RequestBody @Valid PersonDTO personDTO) {
    boolean updated = personService.updatePerson(personDTO);
    if(updated) {
    personService.updatePerson(personDTO);
    return ResponseEntity.ok("Person updated");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Update person failed");
    }
  }

  /**
   * Supprime une personne existante dans la base de donnée via un corps de requête JSON.
   * Le prénom et le nom de famille sont obligatoires.
   */
  @DeleteMapping("/person")
  public ResponseEntity<String> deletePerson(@RequestBody @Valid PersonDTO personDTO) {
    boolean deleted = personService.deletePerson(personDTO);
    if(deleted) {
    personService.deletePerson(personDTO);
    return ResponseEntity.ok("Person deleted");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Delete person failed");
    }
  }

}
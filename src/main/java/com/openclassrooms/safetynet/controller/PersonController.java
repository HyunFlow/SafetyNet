package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.PersonDTO;
import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
import com.openclassrooms.safetynet.service.PersonInfoService;
import com.openclassrooms.safetynet.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des personnes.
 * Fournit des endpoints pour les opérations CRUD sur les personnes et la récupération d'informations personnelles.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PersonController {

  private final PersonInfoService personInfoService;
  private final PersonService personService;

  /**
   * Recherche les informations personnelles des personnes portant un nom de famille donné.
   *
   * @param lastName le nom de famille à rechercher
   * @return List<PersonInfoResponseDTO> contenant les informations des personnes trouvées
   */
  @GetMapping("/personInfolastName")
  public List<PersonInfoResponseDTO> getPersonInfoByLastName(@RequestParam String lastName) {
    log.info("GET request received for person info with lastName: {}", lastName);
    List<PersonInfoResponseDTO> result = personInfoService.findPersonsInfoByLastName(lastName);
    log.info("Response: Found {} person(s) with lastName: {}", result.size(), lastName);
    return result;
  }

  /**
   * Recherche toutes les personnes enregistrées dans la base de données.
   *
   * @return List<PersonDTO> contenant toutes les personnes enregistrées
   */
  @GetMapping("/persons")
  public List<PersonDTO> getAllPersons() {
    log.info("GET request received for all persons");
    List<PersonDTO> persons = personService.findAllPersons();
    log.info("Response: Found {} persons in total", persons.size());
    return persons;
  }

  /**
   * Ajoute une nouvelle personne via un corps de requête JSON.
   *
   * @param personDTO les informations de la personne à créer
   * @return ResponseEntity avec le statut HTTP approprié (201 si créé, 409 si existe déjà)
   */
  @PostMapping("/person")
  public ResponseEntity<String> createNewPerson(@RequestBody @Valid PersonDTO personDTO) {
    log.info("POST request received to create person: {} {}", 
        personDTO.getFirstName(), personDTO.getLastName());
    
    boolean saved = personService.saveNewPerson(personDTO);
    if (saved) {
      log.info("Response: Successfully created person: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.status(HttpStatus.CREATED).body("New person created");
    } else {
      log.error("Response: Failed to create person - already exists: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Create person failed");
    }
  }

  /**
   * Met à jour les informations d'une personne existante dans la base de données.
   *
   * @param personDTO les nouvelles informations de la personne
   * @return ResponseEntity avec le statut HTTP approprié (200 si mis à jour, 409 si échec)
   */
  @PutMapping("/person")
  public ResponseEntity<String> updatePerson(@RequestBody @Valid PersonDTO personDTO) {
    log.info("PUT request received to update person: {} {}", 
        personDTO.getFirstName(), personDTO.getLastName());
    
    boolean updated = personService.updatePerson(personDTO);
    if (updated) {
      log.info("Response: Successfully updated person: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.ok("Person updated");
    } else {
      log.error("Response: Failed to update person - not found: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Update person failed");
    }
  }

  /**
   * Supprime une personne de la base de données.
   *
   * @param personDTO les informations de la personne à supprimer
   * @return ResponseEntity avec le statut HTTP approprié (200 si supprimé, 409 si échec)
   */
  @DeleteMapping("/person")
  public ResponseEntity<String> deletePerson(@RequestBody @Valid PersonDTO personDTO) {
    log.info("DELETE request received for person: {} {}", 
        personDTO.getFirstName(), personDTO.getLastName());
    
    boolean deleted = personService.deletePerson(personDTO);
    if (deleted) {
      log.info("Response: Successfully deleted person: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.ok("Person deleted");
    } else {
      log.error("Response: Failed to delete person - not found: {} {}", 
          personDTO.getFirstName(), personDTO.getLastName());
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Delete person failed");
    }
  }
}
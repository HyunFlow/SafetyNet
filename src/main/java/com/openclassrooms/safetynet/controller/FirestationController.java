package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.service.FirestationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FirestationController {

  private final FirestationService firestationService;

  /**
   * Lire une list des carsernes pompiers.
   */
  @GetMapping("/firestations")
  public List<Firestation> getAllFirestations() {
    return firestationService.getAllFirestations();
  }

  /**
   * Lire une liste de personne associée par un numéro de station.
   */
  @GetMapping("/firestation")
  public FirestationResponseDTO getListOfPeopleByStationNumber(@RequestParam int stationNumber) {
    log.info(String.valueOf(stationNumber));
    log.info(String.valueOf(firestationService.getPeopleByStation(stationNumber)));
    return firestationService.getPeopleByStation(stationNumber);
  }

  /**
   * Ajouter une nouvelle caserne pompier avec une adresses et un numéro de station
   */
  @PostMapping("/firestation")
  public ResponseEntity<String> postFirestationMapping(@RequestParam String address,
      @RequestParam int stationNumber) {
    boolean added = firestationService.addFirestation(address, stationNumber);

    if (added) {
      log.info("New firestation added");
      return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added firestation");
    } else {
      log.info("New firestation failed");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Mapping already exists");
    }
  }

  /**
   * Mettre à jour une caserne pompier avec une adresse et un numéro de station
   */
  @PutMapping("/firestation")
  public ResponseEntity<String> updateFirestation(@RequestParam String address,
      @RequestParam int stationNumber) {
    boolean updated = firestationService.setFirestation(address, stationNumber);

    if (updated) {
      log.info("Updated firestation with address: " + address);
      return ResponseEntity.status(HttpStatus.OK).body("Successfully updated firestation");
    } else {
      log.info("Updated firestation failed");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
    }
  }

  /**
   * Supprimer une caserne pompier par une adresse ou un numéro de station
   */
  @DeleteMapping("/firestation")
  public ResponseEntity<String> deleteFirestation(@RequestParam(required = false) String address,
      @RequestParam(required = false) Integer stationNumber) {

    if (address != null) {
      boolean deleted = firestationService.deleteFirestationByAddress(address);
      return deleted ?
          ResponseEntity.ok().body("Deleted firestation with address:" + address)
          : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
    } else if (stationNumber != null) {
      boolean deleted = firestationService.deleteFirestationByStation(stationNumber);
      return deleted ?
          ResponseEntity.ok().body("Deleted all firestations with stationNumber:" + stationNumber)
          : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Either address or stationNumber must be provided");
    }
  }
}

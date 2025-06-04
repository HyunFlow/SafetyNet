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

/**
 * Contrôleur REST pour la gestion des casernes de pompiers.
 * Fournit des endpoints pour les opérations CRUD sur les casernes et la récupération des informations de couverture.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FirestationController {

  private final FirestationService firestationService;

  /**
   * Récupère la liste de toutes les casernes enregistrées.
   *
   * @return List<Firestation> la liste de toutes les casernes
   */
  @GetMapping("/firestations")
  public List<Firestation> getAllFirestations() {
    log.info("Retrieving all firestations.");
    return firestationService.getAllFirestations();
  }

  /**
   * Récupère la liste des personnes couvertes par un numéro de caserne donné.
   *
   * @param stationNumber le numéro de la caserne
   * @return FirestationResponseDTO contenant la liste des personnes et les statistiques démographiques
   */
  @GetMapping("/firestation")
  public FirestationResponseDTO getListOfPeopleByStationNumber(@RequestParam int stationNumber) {
    log.info("Retrieving people covered by firestation number {}", stationNumber);
    return firestationService.getPeopleByStation(stationNumber);
  }

  /**
   * Ajoute une nouvelle caserne avec une adresse et un numéro de station.
   *
   * @param address l'adresse de la nouvelle caserne
   * @param stationNumber le numéro de la station
   * @return ResponseEntity<String> avec le statut HTTP approprié (201 si créé, 409 si existe déjà)
   */
  @PostMapping("/firestation")
  public ResponseEntity<String> postFirestationMapping(@RequestParam String address, @RequestParam int stationNumber) {
    boolean added = firestationService.addFirestation(address, stationNumber);
    log.info("Adding new firestation: address='{}', stationNumber={}", address, stationNumber);

    if (added) {
      log.info("Firestation successfully added.");
      return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added firestation");
    } else {
      log.info("Failed to add firestation.");
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Mapping already exists");
    }
  }

  /**
   * Met à jour une caserne existante à partir de son adresse et d'un nouveau numéro de station.
   *
   * @param address l'adresse de la caserne à mettre à jour
   * @param stationNumber le nouveau numéro de station
   * @return ResponseEntity<String> avec le statut HTTP approprié (200 si mis à jour, 404 si non trouvé)
   */
  @PutMapping("/firestation")
  public ResponseEntity<String> updateFirestation(@RequestParam String address, @RequestParam int stationNumber) {
    boolean updated = firestationService.setFirestation(address, stationNumber);
    log.info("Updating firestation: address='{}', stationNumber={}", address, stationNumber);

    if (updated) {
      log.info("Firestation successfully updated.");
      return ResponseEntity.status(HttpStatus.OK).body("Successfully updated firestation");
    } else {
      log.info("Failed to update firestation.");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
    }
  }

  /**
   * Supprime une caserne à partir de son adresse ou de son numéro de station.
   *
   * @param address l'adresse de la caserne à supprimer (optionnel)
   * @param stationNumber le numéro de station à supprimer (optionnel)
   * @return ResponseEntity<String> avec le statut HTTP approprié
   * @throws IllegalArgumentException si ni l'adresse ni le numéro de station ne sont fournis
   */
  @DeleteMapping("/firestation")
  public ResponseEntity<String> deleteFirestation(@RequestParam(required = false) String address, @RequestParam(required = false) Integer stationNumber) {
    log.info("Delete request received: address='{}', stationNumber={}", address, stationNumber);

    if (address != null) {
      log.info("Attempting to delete firestation with address '{}'", address);
      boolean deleted = firestationService.deleteFirestationByAddress(address);

      if(deleted) {
        log.info("Firestation successfully deleted.");
        return ResponseEntity.ok().body("Deleted firestation with address: " + address);
      } else {
        log.warn("Firestation with address '{}' not found.", address);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
      }

    } else if (stationNumber != null) {
      log.info("Attempting to delete all firestations with station number {}", stationNumber);
      boolean deleted = firestationService.deleteFirestationByStation(stationNumber);

      if(deleted) {
        log.info("Firestation successfully deleted.");
        return ResponseEntity.ok().body("Deleted firestation with station number: " + stationNumber);
      } else {
        log.warn("No firestations found with station number {}", stationNumber);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation with this station number not found");
      }

    } else {
      log.warn("Delete request rejected: neither address nor stationNumber was provided");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Either address or stationNumber must be provided");
    }
  }
}

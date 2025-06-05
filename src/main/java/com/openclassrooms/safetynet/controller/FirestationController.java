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
    log.info("GET request received for all firestations");
    List<Firestation> firestations = firestationService.getAllFirestations();
    log.info("Response: Found {} firestations", firestations.size());
    return firestations;
  }

  /**
   * Récupère la liste des personnes couvertes par un numéro de caserne donné.
   *
   * @param stationNumber le numéro de la caserne
   * @return FirestationResponseDTO contenant la liste des personnes et les statistiques démographiques
   */
  @GetMapping("/firestation")
  public FirestationResponseDTO getListOfPeopleByStationNumber(@RequestParam int stationNumber) {
    log.info("GET request received for people covered by station number: {}", stationNumber);
    FirestationResponseDTO response = firestationService.getPeopleByStation(stationNumber);
    log.info("Response: Found {} people covered by station {}", 
        response.getPersons().size(), stationNumber);
    return response;
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
    log.info("POST request received to create firestation: address='{}', station={}", 
        address, stationNumber);
    boolean added = firestationService.addFirestation(address, stationNumber);

    if (added) {
      log.info("Response: Successfully created firestation: address='{}', station={}", 
          address, stationNumber);
      return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added firestation");
    } else {
      log.error("Response: Failed to create firestation - already exists: address='{}', station={}", 
          address, stationNumber);
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
    log.info("PUT request received to update firestation: address='{}', station={}", 
        address, stationNumber);
    boolean updated = firestationService.setFirestation(address, stationNumber);

    if (updated) {
      log.info("Response: Successfully updated firestation: address='{}', station={}", 
          address, stationNumber);
      return ResponseEntity.status(HttpStatus.OK).body("Successfully updated firestation");
    } else {
      log.error("Response: Failed to update firestation - not found: address='{}', station={}", 
          address, stationNumber);
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
    log.info("DELETE request received with address='{}', station={}", address, stationNumber);

    if (address != null) {
      log.info("Processing delete request for firestation by address: '{}'", address);
      boolean deleted = firestationService.deleteFirestationByAddress(address);

      if(deleted) {
        log.info("Response: Successfully deleted firestation: address='{}'", address);
        return ResponseEntity.ok().body("Deleted firestation with address: " + address);
      } else {
        log.error("Response: Failed to delete firestation - not found: address='{}'", address);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation address not found");
      }

    } else if (stationNumber != null) {
      log.info("Processing delete request for firestations by station number: {}", stationNumber);
      boolean deleted = firestationService.deleteFirestationByStation(stationNumber);

      if(deleted) {
        log.info("Response: Successfully deleted firestations with station number: {}", stationNumber);
        return ResponseEntity.ok().body("Deleted firestation with station number: " + stationNumber);
      } else {
        log.error("Response: Failed to delete firestations - none found with station number: {}", 
            stationNumber);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Firestation with this station number not found");
      }

    } else {
      log.error("Response: Failed to delete firestation - no address or station number provided");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Either address or stationNumber must be provided");
    }
  }
}

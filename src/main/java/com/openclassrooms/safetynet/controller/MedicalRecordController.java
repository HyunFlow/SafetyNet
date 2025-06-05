package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.service.MedicalRecordService;
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
 * Contrôleur REST pour la gestion des dossiers médicaux.
 * Fournit des endpoints pour les opérations CRUD sur les dossiers médicaux des personnes.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  /**
   * Récupère tous les dossiers médicaux enregistrés.
   *
   * @return List<MedicalRecordResponseDTO> contenant tous les dossiers médicaux
   */
  @GetMapping("/medicalRecords")
  public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
    log.info("GET request received for all medical records");
    List<MedicalRecordResponseDTO> records = medicalRecordService.findAllMedicalRecords();
    log.info("Response: Found {} medical records", records.size());
    return records;
  }

  /**
   * Crée un nouveau dossier médical à partir des données fournies.
   *
   * @param medicalRecordResponseDTO les informations du dossier médical à créer
   * @return ResponseEntity avec le statut HTTP approprié (201 si créé, 409 si existe déjà)
   */
  @PostMapping("/medicalRecord")
  public ResponseEntity<String> createMedicalRecord(
      @RequestBody @Valid MedicalRecordResponseDTO medicalRecordResponseDTO) {
    log.info("POST request received to create medical record for: {} {}", 
        medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
    
    boolean created = medicalRecordService.createNewMedicalRecord(medicalRecordResponseDTO);

    if (created) {
      log.info("Response: Successfully created medical record for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("New medical record created", HttpStatus.CREATED);
    } else {
      log.error("Response: Failed to create medical record - already exists for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("Create a Medical record failed", HttpStatus.CONFLICT);
    }
  }

  /**
   * Met à jour un dossier médical existant.
   *
   * @param medicalRecordResponseDTO les nouvelles informations du dossier médical
   * @return ResponseEntity avec le statut HTTP approprié (200 si mis à jour, 409 si échec)
   */
  @PutMapping("/medicalRecord")
  public ResponseEntity<String> updateMedicalRecord(
      @RequestBody @Valid MedicalRecordResponseDTO medicalRecordResponseDTO) {
    log.info("PUT request received to update medical record for: {} {}", 
        medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
    
    boolean updated = medicalRecordService.updateMedicalRecord(medicalRecordResponseDTO);

    if (updated) {
      log.info("Response: Successfully updated medical record for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("Medical record updated", HttpStatus.OK);
    } else {
      log.error("Response: Failed to update medical record - not found for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("Update a Medical record failed", HttpStatus.CONFLICT);
    }
  }

  /**
   * Supprime un dossier médical.
   *
   * @param medicalRecordResponseDTO les informations du dossier médical à supprimer
   * @return ResponseEntity avec le statut HTTP approprié (200 si supprimé, 409 si échec)
   */
  @DeleteMapping("/medicalRecord")
  public ResponseEntity<String> deleteMedicalRecord(
      @RequestBody @Valid MedicalRecordResponseDTO medicalRecordResponseDTO) {
    log.info("DELETE request received for medical record: {} {}", 
        medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
    
    boolean deleted = medicalRecordService.deleteMedicalRecord(medicalRecordResponseDTO);

    if (deleted) {
      log.info("Response: Successfully deleted medical record for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("Medical record deleted", HttpStatus.OK);
    } else {
      log.error("Response: Failed to delete medical record - not found for: {} {}", 
          medicalRecordResponseDTO.getFirstName(), medicalRecordResponseDTO.getLastName());
      return new ResponseEntity<>("Delete a Medical record failed", HttpStatus.CONFLICT);
    }
  }

  /**
   * Récupère les dossiers médicaux correspondant au prénom et nom donnés.
   *
   * @param firstName le prénom de la personne
   * @param lastName le nom de famille de la personne
   * @return List<MedicalRecordResponseDTO> contenant les dossiers médicaux trouvés
   */
  @GetMapping("/medicalRecord")
  public List<MedicalRecordResponseDTO> getMedicalRecordByName(
      @RequestParam String firstName,
      @RequestParam String lastName) {
    log.info("GET request received for medical records with firstName: {}, lastName: {}", 
        firstName, lastName);
    List<MedicalRecordResponseDTO> records = medicalRecordService.findMedicalRecordsByName(firstName, lastName);
    log.info("Response: Found {} medical record(s) for: {} {}", records.size(), firstName, lastName);
    return records;
  }
}

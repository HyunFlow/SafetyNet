package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.service.MedicalRecordService;
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

/**
 * Contrôleur REST pour la gestion des dossiers médicaux.
 * Fournit des endpoints pour les opérations CRUD sur les dossiers médicaux des personnes.
 */
@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  /**
   * Récupère tous les dossiers médicaux enregistrés.
   *
   * @return List<MedicalRecordResponseDTO> contenant tous les dossiers médicaux
   */
  @GetMapping("/medicalRecords")
  public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
    return medicalRecordService.findAllMedicalRecords();
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
    boolean created = medicalRecordService.createNewMedicalRecord(medicalRecordResponseDTO);

    if (created) {
      return new ResponseEntity<>("New medical record created", HttpStatus.CREATED);
    } else {
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
    boolean updated = medicalRecordService.updateMedicalRecord(medicalRecordResponseDTO);

    if (updated) {
      return new ResponseEntity<>("Medical record updated", HttpStatus.OK);
    } else {
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
    boolean deleted = medicalRecordService.deleteMedicalRecord(medicalRecordResponseDTO);

    if (deleted) {
      return new ResponseEntity<>("Medical record deleted", HttpStatus.OK);
    } else {
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
    return medicalRecordService.findMedicalRecordsByName(firstName, lastName);
  }
}

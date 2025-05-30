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

@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

  private final MedicalRecordService medicalRecordService;

  /**
   * Récupère la liste de tous les dossiers médicaux enregistrés.
   */
  @GetMapping("/medicalRecords")
  public List<MedicalRecordResponseDTO> getAllMedicalRecords() {
    return medicalRecordService.findAllMedicalRecords();
  }

  /**
   * Récupère un ou plusieurs dossiers médicaux correspondant au prénom et nom fournis.
   */
  @GetMapping("/medicalRecord")
  public List<MedicalRecordResponseDTO> getMedicalRecordByName(@RequestParam String firstName,
      @RequestParam String lastName) {
    return medicalRecordService.findMedicalRecordsByName(firstName, lastName);
  }

  /**
   * Crée un nouveau dossier médical à partir des données fournies dans le corps de la requête. Le
   * prénom et le nom sont obligatoires.
   */
  @PostMapping("/medicalRecord")
  public ResponseEntity<String> postNewMedicalRecordByName(
      @RequestBody @Valid MedicalRecordResponseDTO medicalRecordResponseDTO) {
    boolean created = medicalRecordService.createNewMedicalRecord(medicalRecordResponseDTO);

    if (created) {
      return new ResponseEntity<>("New medical record created", HttpStatus.CREATED);
    } else {
      return new ResponseEntity<>("Create a Medical record failed", HttpStatus.CONFLICT);
    }
  }

  /**
   * Met à jour un dossier médical existant avec les données fournies. Le prénom et le nom sont
   * obligatoires pour identifier le dossier à modifier.
   */
  @PutMapping("/medicalRecord")
  public ResponseEntity<String> putMedicalRecord(
      @RequestBody @Valid MedicalRecordResponseDTO medicalRecordResponseDTO) {
    boolean updated = medicalRecordService.updatedMedicalRecord(medicalRecordResponseDTO);

    if (updated) {
      return new ResponseEntity<>("Medical record updated", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Update a Medical record failed", HttpStatus.CONFLICT);
    }
  }

  /**
   * Supprime un dossier médical correspondant aux informations fournies. Le prénom et le nom sont
   * obligatoires pour identifier le dossier à supprimer.
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
}

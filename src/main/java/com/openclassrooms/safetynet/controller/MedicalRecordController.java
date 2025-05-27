package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.service.MedicalRecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MedicalRecordController {
  private final MedicalRecordService medicalRecordService;

  /**
   * Recherche le dossier médical correspondant à un prénom et un nom de famille.
   */
  @GetMapping("/medicalRecord")
  public List<MedicalRecordResponseDTO> getMedicalRecordByName(@RequestParam String firstName, @RequestParam String lastName) {
    return medicalRecordService.findMedicalRecordsByName(firstName, lastName);
  }
}

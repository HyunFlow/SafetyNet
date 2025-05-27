package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

  private final DataRepository dataRepository;

  /**
   * Recherche les dossiers médicaux correspondant à un prénom et un nom de famille.
   */
  public List<MedicalRecordResponseDTO> findMedicalRecordsByName(String firstName,
      String lastName) {
    return dataRepository.getMedicalRecords().stream()
        .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName) && mr.getLastName()
            .equalsIgnoreCase(lastName))
        .map(mr -> new MedicalRecordResponseDTO(
            mr.getFirstName(),
            mr.getLastName(),
            mr.getBirthdate(),
            mr.getMedications(),
            mr.getAllergies()
        ))
        .collect(Collectors.toList());
  }

  /**
   * Recherche les médicaments correspondant à un prénom et un nom de famille.
   */
  public List<String> findMedicationsByName(String firstName, String lastName) {
    return dataRepository.getMedicalRecords().stream()
        .filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName()
            .equalsIgnoreCase(lastName))
        .flatMap(r -> r.getMedications().stream())
        .collect(Collectors.toList());
  }

  /**
   * Recherche les allergies correspondant à un prénom et un nom de famille.
   */
  public List<String> findAllergiesByName(String firstName, String lastName) {
    return dataRepository.getMedicalRecords().stream()
        .filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName()
            .equalsIgnoreCase(lastName))
        .flatMap(r -> r.getAllergies().stream())
        .collect(Collectors.toList());
  }
}

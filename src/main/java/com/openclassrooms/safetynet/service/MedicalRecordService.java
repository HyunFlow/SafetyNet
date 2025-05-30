package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

  public boolean createNewMedicalRecord(MedicalRecordResponseDTO newRecordDTO) {
    boolean existingRecord = existMedicalRecord(newRecordDTO);

    if(!existingRecord) {
      MedicalRecord newRecord = new MedicalRecord(
          newRecordDTO.getFirstName(),
          newRecordDTO.getLastName(),
          newRecordDTO.getBirthdate(),
          newRecordDTO.getMedications(),
          newRecordDTO.getAllergies()
      );
          return dataRepository.addMedicalRecord(newRecord);
    }
    return false;
  }

  public List<MedicalRecordResponseDTO> findAllMedicalRecords() {
    return dataRepository.getMedicalRecords().stream()
        .map(r -> new MedicalRecordResponseDTO(
            r.getFirstName(),
            r.getLastName(),
            r.getBirthdate(),
            r.getMedications(),
            r.getAllergies()
        )).collect(Collectors.toList());
  }

  public boolean updatedMedicalRecord(MedicalRecordResponseDTO recordDto) {
    boolean existingRecord = existMedicalRecord(recordDto);

    if(existingRecord) {
      MedicalRecord newRecord = mapDtoToMedicalRecord(recordDto);
      return dataRepository.setMedicalRecord(newRecord);
    } else {
      log.error("Medical record not found");
      return false;
    }
  }

  public boolean deleteMedicalRecord(MedicalRecordResponseDTO recordDto) {
    boolean existingRecord = existMedicalRecord(recordDto);

    if(existingRecord) {
      MedicalRecord medicalRecord = mapDtoToMedicalRecord(recordDto);
      return dataRepository.deleteMedicalRecord(medicalRecord);
    } else {
      log.error("Medical record not found");
      return false;
    }
  }

  private boolean existMedicalRecord(MedicalRecordResponseDTO recordDto) {
    return dataRepository.getMedicalRecords().stream()
        .anyMatch(mr -> mr.getFirstName().equalsIgnoreCase(recordDto.getFirstName()) &&
            mr.getLastName().equalsIgnoreCase(recordDto.getLastName()));
  }

  private MedicalRecord mapDtoToMedicalRecord(MedicalRecordResponseDTO dto) {
    MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setFirstName(dto.getFirstName());
        medicalRecord.setLastName(dto.getLastName());
        medicalRecord.setBirthdate(dto.getBirthdate());
        medicalRecord.setMedications(dto.getMedications());
        medicalRecord.setAllergies(dto.getAllergies());
        return medicalRecord;
  }
}

package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsable de la gestion des dossiers médicaux.
 * Fournit des opérations CRUD ainsi que des fonctionnalités associées aux
 * médicaments et allergies des patients. Respecte le principe de responsabilité unique.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordService {

  private final DataRepository dataRepository;

  /**
   * Recherche tous les dossiers médicaux correspondant au prénom et au nom spécifiés.
   * La comparaison des noms est insensible à la casse.
   *
   * @param firstName Le prénom de la personne recherchée
   * @param lastName  Le nom de famille de la personne recherchée
   * @return Une liste de dossiers médicaux correspondants, ou une liste vide si aucun trouvé
   */
  public List<MedicalRecordResponseDTO> findMedicalRecordsByName(String firstName,
      String lastName) {
    List<MedicalRecordResponseDTO> records = dataRepository.getMedicalRecords().stream()
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

    log.debug("Found {} medical records for person: '{}' '{}'", records.size(), firstName, lastName);
    return records;
  }

  /**
   * Recherche les médicaments d'une personne en fonction de son prénom et de son nom.
   * La recherche est insensible à la casse.
   *
   * @param firstName Le prénom de la personne
   * @param lastName  Le nom de famille de la personne
   * @return Une liste de médicaments prescrits
   */
  public List<String> findMedicationsByName(String firstName, String lastName) {
    List<String> medications = dataRepository.getMedicalRecords().stream()
        .filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName()
            .equalsIgnoreCase(lastName))
        .flatMap(r -> r.getMedications().stream())
        .collect(Collectors.toList());

    log.debug("Found {} medications for person: '{}' '{}'", medications.size(), firstName, lastName);
    return medications;
  }

  /**
   * Recherche les allergies connues d'une personne à partir de son prénom et de son nom.
   * La recherche est insensible à la casse.
   *
   * @param firstName Le prénom de la personne
   * @param lastName  Le nom de famille de la personne
   * @return Une liste d'allergies connues
   */
  public List<String> findAllergiesByName(String firstName, String lastName) {
    List<String> allergies = dataRepository.getMedicalRecords().stream()
        .filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName()
            .equalsIgnoreCase(lastName))
        .flatMap(r -> r.getAllergies().stream())
        .collect(Collectors.toList());

    log.debug("Found {} allergies for person: '{}' '{}'", allergies.size(), firstName, lastName);
    return allergies;
  }

  /**
   * Récupère tous les dossiers médicaux enregistrés.
   *
   * @return Une liste de tous les dossiers médicaux disponibles
   */
  public List<MedicalRecordResponseDTO> findAllMedicalRecords() {
    List<MedicalRecordResponseDTO> records = dataRepository.getMedicalRecords().stream()
        .map(mr -> new MedicalRecordResponseDTO(
            mr.getFirstName(),
            mr.getLastName(),
            mr.getBirthdate(),
            mr.getMedications(),
            mr.getAllergies()
        ))
        .collect(Collectors.toList());

    log.debug("Retrieved {} total medical records", records.size());
    return records;
  }

  /**
   * Crée un nouveau dossier médical si aucun dossier existant ne correspond au prénom et nom donnés
   * (comparaison insensible à la casse).
   *
   * @param medicalRecordDTO Les données du nouveau dossier médical
   * @return {@code true} si la création a réussi, {@code false} si un dossier existe déjà
   */
  public boolean createNewMedicalRecord(MedicalRecordResponseDTO medicalRecordDTO) {
    boolean existingRecord = existMedicalRecord(medicalRecordDTO);

    if(!existingRecord) {
      MedicalRecord newRecord = new MedicalRecord(
          medicalRecordDTO.getFirstName(),
          medicalRecordDTO.getLastName(),
          medicalRecordDTO.getBirthdate(),
          medicalRecordDTO.getMedications(),
          medicalRecordDTO.getAllergies()
      );
          return dataRepository.addMedicalRecord(newRecord);
    }
    return false;
  }

  /**
   * Met à jour un dossier médical existant selon le prénom et le nom.
   * Aucune mise à jour n’est effectuée si aucun dossier correspondant n’est trouvé.
   *
   * @param medicalRecordDTO Les nouvelles données à appliquer
   * @return {@code true} si la mise à jour a été effectuée, {@code false} sinon
   */
  public boolean updateMedicalRecord(MedicalRecordResponseDTO medicalRecordDTO) {
    boolean existingRecord = existMedicalRecord(medicalRecordDTO);

    if(existingRecord) {
      MedicalRecord newRecord = mapDtoToMedicalRecord(medicalRecordDTO);
      return dataRepository.setMedicalRecord(newRecord);
    } else {
      log.error("Medical record not found");
      return false;
    }
  }

  /**
   * Supprime un dossier médical existant.
   * La suppression ne s’effectue que si le dossier est trouvé.
   *
   * @param medicalRecordDTO Les informations du dossier médical à supprimer
   * @return {@code true} si la suppression est réussie, {@code false} si le dossier n’existe pas
   */
  public boolean deleteMedicalRecord(MedicalRecordResponseDTO medicalRecordDTO) {
    boolean existingRecord = existMedicalRecord(medicalRecordDTO);

    if(existingRecord) {
      MedicalRecord medicalRecord = mapDtoToMedicalRecord(medicalRecordDTO);
      return dataRepository.deleteMedicalRecord(medicalRecord);
    } else {
      log.error("Medical record not found");
      return false;
    }
  }

  /**
   * Vérifie si un dossier médical existe déjà pour un prénom et un nom donnés (insensible à la casse).
   *
   * @param medicalRecordDTO Le dossier médical à vérifier
   * @return {@code true} si un dossier correspondant existe, {@code false} sinon
   */
  private boolean existMedicalRecord(MedicalRecordResponseDTO medicalRecordDTO) {
    return dataRepository.getMedicalRecords().stream()
        .anyMatch(mr -> mr.getFirstName().equalsIgnoreCase(medicalRecordDTO.getFirstName()) &&
            mr.getLastName().equalsIgnoreCase(medicalRecordDTO.getLastName()));
  }

  /**
   * Convertit un DTO de type {@link MedicalRecordResponseDTO} en entité {@link MedicalRecord}.
   *
   * @param dto Le DTO à convertir
   * @return L’entité {@link MedicalRecord} correspondante
   */
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

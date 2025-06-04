package com.openclassrooms.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.openclassrooms.safetynet.dto.MedicalRecordResponseDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

  @Mock
  private DataRepository dataRepository;

  @InjectMocks
  private MedicalRecordService medicalRecordService;

  @Test
  public void findMedicalRecordsByName_shouldReturnMatchingRecords() {
    // given
    String firstName = "John";
    String lastName = "Boyd";

    List<MedicalRecord> medicalRecords = List.of(
        new MedicalRecord("John", "Boyd", null, null, null),
        new MedicalRecord("Jacob", "Boyd", null, null, null));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);

    // when
    List<MedicalRecordResponseDTO> result = medicalRecordService.findMedicalRecordsByName(firstName,
        lastName);

    // then
    assertThat(result).hasSize(1);
    MedicalRecordResponseDTO dto = result.get(0);
    assertThat(dto.getFirstName()).isEqualTo(firstName);
    assertThat(dto.getLastName()).isEqualTo(lastName);
  }

  @Test
  public void findMedicationByName_shouldReturnMatchingMedications() {
    String firstName = "John";
    String lastName = "Boyd";

    List<String> medications = List.of("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = List.of("nillacilan");

    List<MedicalRecord> medicalRecords = List.of(
        new MedicalRecord("John", "Boyd", null, medications, allergies),
        new MedicalRecord("Jacob", "Boyd", null, null, null));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);

    // when
    List<String> result = medicalRecordService.findMedicationsByName(firstName, lastName);

    // then
    assertThat(result).hasSize(2);
    assertThat(result).containsExactly("aznol:350mg", "hydrapermazol:100mg");
  }

  @Test
  public void findAllergiesByName_shouldReturnMatchingAllergies() {
    String firstName = "John";
    String lastName = "Boyd";

    List<String> medications = List.of("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = List.of("nillacilan");

    List<MedicalRecord> medicalRecords = List.of(
        new MedicalRecord("John", "Boyd", "03/06/1984", medications, allergies),
        new MedicalRecord("Jacob", "Boyd", "03/06/1989", null, null));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);

    // when
    List<String> result = medicalRecordService.findAllergiesByName(firstName, lastName);

    // then
    assertThat(result).hasSize(1);
    assertThat(result).containsExactly("nillacilan");
  }

  @Test
  public void createNewMedicalRecordTest_shouldReturnTrue_whenRecordIsNotExisting() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("John", "Boyd", null, null, null)));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    List<String> medications = List.of("aznol:350mg", "hydrapermazol:100mg");
    List<String> allergies = List.of("nillacilan");

    MedicalRecordResponseDTO newRecord = new MedicalRecordResponseDTO("Jacob", "Boyd", "03/06/1989",
        medications, allergies);

    Mockito.when(dataRepository.addMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

    // when
    boolean result = medicalRecordService.createNewMedicalRecord(newRecord);

    // then
    assertThat(result).isTrue();
  }

  @Test
  public void createNewMedicalRecordTest_shouldReturnFalse_whenRecordIsExisting() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("Jacob", "Boyd", null, null, null)));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    MedicalRecordResponseDTO newRecord = new MedicalRecordResponseDTO("Jacob", "Boyd", null, null,
        null);

    // when
    boolean result = medicalRecordService.createNewMedicalRecord(newRecord);

    // then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).addMedicalRecord(any(MedicalRecord.class));
  }

  @Test
  public void findAllMedicalRecords_shouldReturnListOfMedicalRecordDto() {
    // given
    List<MedicalRecord> medicalRecords = List.of(
        new MedicalRecord("Jacob", "Boyd", null, null, null),
        new MedicalRecord("John", "Boyd", null, null, null));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(medicalRecords);

    // when
    List<MedicalRecordResponseDTO> result = medicalRecordService.findAllMedicalRecords();

    // then
    assertThat(result).hasSize(2);
    MedicalRecordResponseDTO firstRecord = result.get(0);
    assertThat(firstRecord.getFirstName()).isEqualTo("Jacob");
    assertThat(firstRecord.getLastName()).isEqualTo("Boyd");
  }

  @Test
  public void updateMedicalRecord_shouldReturnTrue_whenMedicalRecordIsExisting() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("Jacob", "Boyd", null, null, null)));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    List<String> medications = List.of("doliprane:500mg");
    List<String> allergies = List.of("pollen");

    MedicalRecordResponseDTO updateRecord = new MedicalRecordResponseDTO("Jacob", "Boyd", "03/06/1989", medications, allergies);
    Mockito.when(dataRepository.setMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

    // when
    boolean result = medicalRecordService.updateMedicalRecord(updateRecord);

    // then
    assertThat(result).isTrue();
    Mockito.verify(dataRepository, Mockito.times(1)).setMedicalRecord(any(MedicalRecord.class));
  }

  @Test
  public void updateMedicalRecord_shouldReturnFalse_whenMedicalRecordDoesNotExist() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("Jacob", "Boyd", null, null, null)));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    MedicalRecordResponseDTO updateRecord = new MedicalRecordResponseDTO("John", "Boyd",
        null, null, null);

    // when
    boolean result = medicalRecordService.updateMedicalRecord(updateRecord);

    // then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).setMedicalRecord(any());
  }


  @Test
  public void deleteMedicalRecord_shouldReturnTrue_whenMedicalRecordIsExisting() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("Jacob", "Boyd", "03/06/1989", List.of("medication1"), List.of("allergy1"))));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    MedicalRecordResponseDTO deleteRecord = new MedicalRecordResponseDTO("Jacob", "Boyd", null,
        null, null);
    Mockito.when(dataRepository.deleteMedicalRecord(any(MedicalRecord.class))).thenReturn(true);

    // when
    boolean result = medicalRecordService.deleteMedicalRecord(deleteRecord);

    // then
    assertThat(result).isTrue();
    Mockito.verify(dataRepository, Mockito.times(1)).deleteMedicalRecord(any(MedicalRecord.class));
  }

  @Test
  public void deleteMedicalRecord_shouldReturnFalse_whenMedicalRecordDoesNotExist() {
    // given
    List<MedicalRecord> existingRecord = new ArrayList<>(List.of(
        new MedicalRecord("John", "Boyd", null, null, null)));
    Mockito.when(dataRepository.getMedicalRecords()).thenReturn(existingRecord);

    MedicalRecordResponseDTO deleteRecord = new MedicalRecordResponseDTO("Jacob", "Boyd", null,
        null, null);

    // when
    boolean result = medicalRecordService.deleteMedicalRecord(deleteRecord);

    // then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).deleteMedicalRecord(any());
  }

}

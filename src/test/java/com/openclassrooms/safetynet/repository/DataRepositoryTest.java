package com.openclassrooms.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DataRepositoryTest {

  @Mock
  private DataRepository dataRepository;

  @BeforeEach
  void setUp() {
    dataRepository = new DataRepository();
    dataRepository.setPersons(new ArrayList<>());
    dataRepository.setFirestations(new ArrayList<>());
    dataRepository.setMedicalRecords(new ArrayList<>());
  }

  @Test
  void addFirestation_shouldAddNewFirestationToList() {
    // given
    Firestation newFirestation = new Firestation("123 Winston St", 1);

    // when
    dataRepository.addFirestation(newFirestation);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(1);
    assertThat(result).contains(newFirestation);
  }

  @Test
  void updateFirestation_shouldReplaceFirestationWithSameAddress() {
    // given
    Firestation existFirestation = new Firestation("123 Paul St", 1);
    dataRepository.addFirestation(existFirestation);

    Firestation newMappingFirestation = new Firestation("123 Paul St", 4);

    // when
    dataRepository.setFirestation(newMappingFirestation);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(1);
    Firestation updated = result.get(0);
    assertThat(updated.getStation()).isEqualTo(4);
  }

  @Test
  void deleteFirestation_shouldDeleteFirestationWithSameAddress() {
    // given
    Firestation existFirestation = new Firestation("123 Paul St", 1);
    dataRepository.addFirestation(existFirestation);

    String inputAddress = "123 Paul St";

    // when
    dataRepository.deleteFirestationByAddress(inputAddress);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void deleteFirestation_shouldDeleteFirestationWithSameStationNumber() {
    // given
    Firestation existFirestation = new Firestation("123 Paul St", 1);
    dataRepository.addFirestation(existFirestation);

    int inputStationNumber = 1;

    // when
    dataRepository.deleteFirestationByStation(inputStationNumber);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).isEmpty();
  }

  @Test
  void getAllFirestations_shouldReturnAllFirestations() {
    // given

    Firestation firestation1 = new Firestation("123 Paul St", 1);
    dataRepository.addFirestation(firestation1);

    Firestation firestation2 = new Firestation("533 Roland St", 4);
    dataRepository.addFirestation(firestation2);

    // when
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(2);
  }

  @Test
  void addPerson_shouldReturnTrue() {
    // given
    Person newPerson = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 41);

    // when
    boolean result = dataRepository.addPerson(newPerson);

    // then
    assertThat(result).isTrue();
    assertThat(dataRepository.getPersons()).hasSize(1);
  }

  @Test
  void setPerson_shouldReturnTrue() {
    // given
    Person personExist = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 41);
    dataRepository.addPerson(personExist);

    Person inputPerson = new Person("John", "Boyd", "533 Roland St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 10);

    // when
    boolean result = dataRepository.setPerson(inputPerson);

    // then
    assertThat(result).isTrue();
    Person resultPerson = dataRepository.getPersons().get(0);
    assertThat(resultPerson.getAddress()).isEqualTo("533 Roland St");
    assertThat(resultPerson.getAge()).isEqualTo(10);
  }

  @Test
  void setPerson_shouldReturnFalse() {
    // given
    Person personExist = new Person();
    personExist.setFirstName("Jacob");
    personExist.setLastName("Boyd");
    dataRepository.addPerson(personExist);

    Person inputPerson = new Person("John", "Boyd", "533 Roland St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 10);

    // when
    boolean result = dataRepository.setPerson(inputPerson);

    // then
    assertThat(result).isFalse();
    assertThat(dataRepository.getPersons()).hasSize(1);
  }

  @Test
  void deletePerson_shouldReturnTrue() {
    // given
    Person personExist = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 41);
    dataRepository.addPerson(personExist);

    Person inputPerson = new Person();
    inputPerson.setFirstName("John");
    inputPerson.setLastName("Boyd");

    // when
    boolean result = dataRepository.deletePerson(inputPerson);

    // then
    assertThat(result).isTrue();
    assertThat(dataRepository.getPersons()).hasSize(0);
  }

  @Test
  void deletePerson_shouldReturnFalse() {
    // given
    Person personExist = new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512","jaboyd@email.com", 41);
    dataRepository.addPerson(personExist);

    Person inputPerson = new Person();
    inputPerson.setFirstName("Jacob");
    inputPerson.setLastName("Boyd");

    // when
    boolean result = dataRepository.deletePerson(inputPerson);

    // then
    assertThat(result).isFalse();
    assertThat(dataRepository.getPersons()).hasSize(1);
  }

  @Test
  void addMedicalRecord_shouldReturnTrue() {
    // given
    MedicalRecord newMedicalRecord = new MedicalRecord();
    newMedicalRecord.setFirstName("John");
    newMedicalRecord.setLastName("Boyd");
    newMedicalRecord.setBirthdate("22/06/1992");
    newMedicalRecord.setMedications(List.of("medication1", "medication2"));
    newMedicalRecord.setAllergies(List.of("allergy1", "allergy2"));

    // when
    boolean result = dataRepository.addMedicalRecord(newMedicalRecord);

    // then
    assertThat(result).isTrue();
    assertThat(dataRepository.getMedicalRecords()).hasSize(1);
  }

  @Test
  void setMedicalRecord_shouldReturnTrue() {
    // given
    MedicalRecord existRecord = new MedicalRecord();
    existRecord.setFirstName("John");
    existRecord.setLastName("Boyd");
    existRecord.setBirthdate("22/06/1992");
    existRecord.setMedications(List.of());
    existRecord.setAllergies(List.of());
    dataRepository.addMedicalRecord(existRecord);

    MedicalRecord inputMedicalRecord = new MedicalRecord();
    inputMedicalRecord.setFirstName("John");
    inputMedicalRecord.setLastName("Boyd");
    inputMedicalRecord.setBirthdate("22/06/1992");
    inputMedicalRecord.setMedications(List.of("medication1", "medication2"));
    inputMedicalRecord.setAllergies(List.of("allergy1", "allergy2"));

    // when
    boolean result = dataRepository.setMedicalRecord(inputMedicalRecord);

    // then
    assertThat(result).isTrue();
    MedicalRecord resultMedicalRecord = dataRepository.getMedicalRecords().get(0);
    assertThat(resultMedicalRecord.getMedications()).hasSize(2);
    assertThat(resultMedicalRecord.getAllergies()).hasSize(2);
  }

  @Test
  void deleteMedicalRecord_shouldReturnTrue() {
    // given
    MedicalRecord existRecord = new MedicalRecord();
    existRecord.setFirstName("John");
    existRecord.setLastName("Boyd");
    existRecord.setMedications(List.of("medication1", "medication2"));
    dataRepository.addMedicalRecord(existRecord);

    MedicalRecord inputMedicalRecord = new MedicalRecord();
    inputMedicalRecord.setFirstName("John");
    inputMedicalRecord.setLastName("Boyd");

    // when
    boolean result = dataRepository.deleteMedicalRecord(inputMedicalRecord);

    // then
    assertThat(result).isTrue();
    assertThat(dataRepository.getMedicalRecords()).hasSize(0);
  }

}
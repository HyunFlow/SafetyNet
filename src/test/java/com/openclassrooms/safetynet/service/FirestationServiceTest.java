package com.openclassrooms.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.openclassrooms.safetynet.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

  @Mock
  private DataRepository dataRepository;

  @InjectMocks
  private FirestationService firestationService;

  /** Teste pour avoir une liste de personne par un numéro de caserne */
  @Test
  void getPeopleByStation_shouldReturnCorrectCountsAndListOfPeople() {
    // given
    int stationNumber = 3;

    List<Firestation> mockFirestations = List.of(
        new Firestation("644 Gershwin Cir", 1),
        new Firestation("1509 Culver St", 3)
    );

    List<Person> mockPeople = List.of(
        new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
            "jaboyd@email.com", 41),
        new Person("Roger", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512",
            "tenz@email.com", 7),
        new Person("Peter", "Duncan", "644 Gershwin Cir", "Culver", "97451", "841-874-6512",
            "jaboyd@email.com", 24)
    );

    // when
    when(dataRepository.getFirestations()).thenReturn(mockFirestations);
    when(dataRepository.getPersons()).thenReturn(mockPeople);

    FirestationResponseDTO result = firestationService.getPeopleByStation(stationNumber);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getPersons()).hasSize(2);
    assertThat(result.getAdultCount()).isEqualTo(1);
    assertThat(result.getChildCount()).isEqualTo(1);
  }

  @Test
  void addFirestation_shouldReturnFalse_whenAddressAlreadyExists() {
    // given
    List<Firestation> existingFirestations = List.of(
        new Firestation("644 Gershwin Cir", 1)
    );
    Mockito.when(dataRepository.getFirestations()).thenReturn(existingFirestations);

    // when
    boolean result = firestationService.addFirestation("644 Gershwin Cir", 2);

    // then
    Mockito.verify(dataRepository, Mockito.never()).addFirestation(Mockito.any());
    assertThat(result).isFalse();
  }

  @Test
  void addFirestation_shouldReturnTrue_whenAddressIsNew() {
    // given
    List<Firestation> existingFirestations = List.of();
    Mockito.when(dataRepository.getFirestations()).thenReturn(existingFirestations);

    // when
    boolean result = firestationService.addFirestation("150 Paul Reiss", 2);

    // then
    Mockito.verify(dataRepository).addFirestation(new Firestation("150 Paul Reiss", 2));
    assertThat(result).isTrue();
  }

  @Test
  void setFirestation_shouldReturnTrue_whenAddressAlreadyExists() {
    // given
    List<Firestation> existingFirestations = List.of(new Firestation("1509 Culver St", 3));
    Mockito.when(dataRepository.getFirestations()).thenReturn(existingFirestations);

    // when
    boolean result = firestationService.setFirestation("1509 Culver St", 2);

    // then
    Mockito.verify(dataRepository).updateFirestation(new Firestation("1509 Culver St", 2));
    assertThat(result).isTrue();
  }

  @Test
  void deleteFirestationByAddress_shouldReturnFalse_whenAddressDoesNotExist() {
    // given
    List<Firestation> existingFirestations = List.of();
    Mockito.when(dataRepository.getFirestations()).thenReturn(existingFirestations);

    // when
    boolean result = firestationService.deleteFirestationByAddress("1509 Culver St");

    // then
    Mockito.verify(dataRepository, Mockito.never()).deleteFirestationByAddress(Mockito.anyString());
    assertThat(result).isFalse();
  }

  @Test
  void deleteFirestationByStation_shouldReturnFalse_whenAddressDoesNotExist() {
    // given
    List<Firestation> existingFirestations = List.of();
    Mockito.when(dataRepository.getFirestations()).thenReturn(existingFirestations);

    // when
    boolean result = firestationService.deleteFirestationByStation(2);

    // then
    Mockito.verify(dataRepository, Mockito.never()).deleteFirestationByStation(Mockito.anyInt());
    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnAllFirestations() {
    // given
    List<Firestation> existingFirestations = List.of(
        new Firestation("644 Gershwin Cir", 1),
        new Firestation("1509 Culver St", 3)
    );
    Mockito.when(dataRepository.getAllFirestations()).thenReturn(existingFirestations);

    // when
    List<Firestation> result = firestationService.getAllFirestations();

    // then
    Mockito.verify(dataRepository, Mockito.times(1)).getAllFirestations();
    assertThat(result).isNotNull();
    assertThat(result).isEqualTo(existingFirestations);
  }
}
package com.openclassrooms.safetynet.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.safetynet.model.Firestation;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DataRepositoryTest {

  private DataRepository dataRepository;

  @BeforeEach
  void setUp() {
    dataRepository = new DataRepository();
    List<Firestation> firestations = List.of(new Firestation("123 Paul St", 1));
    dataRepository.setFirestations(new ArrayList<>(firestations));
  }

  @Test
  void addFirestation_shouldAddNewFirestationToList() {
    // given
    Firestation newFirestation = new Firestation("123 Winston St", 1);

    // when
    dataRepository.addFirestation(newFirestation);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(2);
    assertThat(result).contains(newFirestation);
  }

  @Test
  void updateFirestation_shouldReplaceFirestationWithSameAddress() {
    // given
    Firestation newMappingFirestation = new Firestation("123 Paul St", 4);

    // when
    dataRepository.updateFirestation(newMappingFirestation);
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(1);

    Firestation updated = result.get(0);
    assertThat(updated.getAddress()).isEqualTo("123 Paul St");
    assertThat(updated.getStation()).isEqualTo(4);
  }

  @Test
  void deleteFirestation_shouldDeleteFirestationWithSameAddress() {
    // given
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
    Firestation newFirestation = new Firestation("533 Roland St", 4);
    dataRepository.addFirestation(newFirestation);

    // when
    List<Firestation> result = dataRepository.getAllFirestations();

    // then
    assertThat(result).hasSize(2);
  }
}
package com.openclassrooms.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.safetynet.dto.FloodResponseDTO;
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
public class FloodServiceTest {

  @Mock
  private DataRepository dataRepository;
  @Mock
  private MedicalRecordService medicalRecordService;
  @InjectMocks
  private FloodService floodService;

  @Test
  public void findHouseHoldByStation_shouldReturnListOfFloodDto_whenStationGiven() {
    // given
    List<Firestation> firestations = List.of(
        new Firestation("1509 Culver St", 3),
        new Firestation("29 15th St", 2)
    );

    List<Person> persons = List.of(
        new Person("John", "Boyd", "1509 Culver St",null, null, null, null, 0),
        new Person("Jacob", "Boyd", "29 15th St", null, null, null, null, 0),
        new Person("Foster", "Shepard", "748 Townings Dr", null, null, null, null, 0)
    );

    Mockito.when(dataRepository.getFirestations()).thenReturn(firestations);
    Mockito.when(dataRepository.getPersons()).thenReturn(persons);

    Mockito.when(medicalRecordService.findMedicationsByName("John", "Boyd")).thenReturn(List.of("medication1"));
    Mockito.when(medicalRecordService.findAllergiesByName("John", "Boyd")).thenReturn(List.of("allergy1", "allergy2"));
    Mockito.when(medicalRecordService.findMedicationsByName("Jacob", "Boyd")).thenReturn(List.of("medication1"));
    Mockito.when(medicalRecordService.findAllergiesByName("Jacob", "Boyd")).thenReturn(List.of("allergy1", "allergy2"));

    // when
    List<FloodResponseDTO> result = floodService.findHouseholdsByStationNumbers(List.of(3,2));

    // then
    assertThat(result).hasSize(2);

    FloodResponseDTO dto1 = result.stream().filter(r -> r.getAddress().equals("1509 Culver St")).findFirst().orElseThrow();
    assertThat(dto1.getStationNumber()).isEqualTo(3);
    assertThat(dto1.getResidents()).hasSize(1);
    assertThat(dto1.getResidents().get(0).getFirstName()).isEqualTo("John");

    FloodResponseDTO dto2 = result.stream().filter(r -> r.getAddress().equals("29 15th St")).findFirst().orElseThrow();
    assertThat(dto2.getStationNumber()).isEqualTo(2);
    assertThat(dto2.getResidents()).hasSize(1);
    assertThat(dto2.getResidents().get(0).getFirstName()).isEqualTo("Jacob");

    Mockito.verify(medicalRecordService, Mockito.times(1)).findMedicationsByName("John", "Boyd");
    Mockito.verify(medicalRecordService, Mockito.times(1)).findAllergiesByName("John", "Boyd");
  }

}

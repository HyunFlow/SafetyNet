package com.openclassrooms.safetynet.service;


import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.dto.ResidentDTO;
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
public class FireServiceTest {
  @Mock
  private DataRepository dataRepository;
  @Mock
  private FirestationService firestationService;
  @Mock
  private MedicalRecordService medicalRecordService;
  @InjectMocks
  private FireService fireService;

  @Test
  public void findResidentsByAddress_shouldReturnFireDto_whenAddressGiven() {
    // given
    String inputAddress = "1509 Culver St";

    List<Person> persons = List.of(
        new Person("John", "Boyd", "1509 Culver St", null, null, null, null,41),
        new Person("Jacob", "Boyd", "1509 Culver St",null, null, null, null,36),
        new Person("Shawna", "Stelzer", "947 E. Rose Dr", null,null,null,null,55)
    );

    Mockito.when(firestationService.getStationNumberByAddress(inputAddress)).thenReturn(3);
    Mockito.when(dataRepository.getPersons()).thenReturn(persons);
    Mockito.when(medicalRecordService.findMedicationsByName("John", "Boyd")).thenReturn(List.of("medication1"));
    Mockito.when(medicalRecordService.findAllergiesByName("John", "Boyd")).thenReturn(List.of("allergy1", "allergy2"));
    Mockito.when(medicalRecordService.findMedicationsByName("Jacob", "Boyd")).thenReturn(List.of("medication2"));
    Mockito.when(medicalRecordService.findAllergiesByName("Jacob", "Boyd")).thenReturn(List.of());

    // when
    FireResponseDTO result = fireService.findResidentsByAddress(inputAddress);

    // then
    assertThat(result.getStationNumber()).isEqualTo(3);
    assertThat(result.getResidents()).hasSize(2);

    ResidentDTO resident1 = result.getResidents().get(0);
    assertThat(resident1.getFirstName()).isEqualTo("John");
    assertThat(resident1.getLastName()).isEqualTo("Boyd");
    assertThat(resident1.getMedications()).containsExactly("medication1");
    assertThat(resident1.getAllergies()).containsExactly("allergy1","allergy2");

    ResidentDTO resident2 = result.getResidents().get(1);
    assertThat(resident2.getFirstName()).isEqualTo("Jacob");
    assertThat(resident2.getLastName()).isEqualTo("Boyd");
    assertThat(resident2.getMedications()).containsExactly("medication2");
    assertThat(resident2.getAllergies()).isEmpty();
  }
}

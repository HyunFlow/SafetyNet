package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.PersonInfoResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonInfoService {

  private final DataRepository dataRepository;
  private final MedicalRecordService medicalRecordService;

  /**
   * Retourne les informations personnelles des personnes dont le nom de famille correspond au paramètre donné.
   */
  public List<PersonInfoResponseDTO> findPersonsInfoByLastName(String lastName) {
    List<Person> persons = dataRepository.getPersons();
    return persons.stream()
        .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
        .map(p -> {
          return new PersonInfoResponseDTO(
              p.getLastName(),
              p.getAddress(),
              p.getAge(),
              p.getEmail(),
              medicalRecordService.findMedicationsByName(p.getFirstName(), p.getLastName()),
              medicalRecordService.findAllergiesByName(p.getFirstName(), p.getLastName())
          );
        })
        .collect(Collectors.toList());
  }
}

package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.PhoneAlertResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneAlertService {
  private final FirestationService firestationService;

  /**
   * Récupère les numéros de téléphone des personnes couvertes par une caserne spécifique.
   */
  public PhoneAlertResponseDTO findPhoneNumberOfPeopleByFirestation(int stationNumber) {
    List<Person> persons = firestationService.getPeopleByStation(stationNumber).getPersons();
    List<String> phoneNumber = persons.stream()
        .map(Person::getPhone)
        .distinct()
        .collect(Collectors.toList());

    return new PhoneAlertResponseDTO(phoneNumber);
  }
}

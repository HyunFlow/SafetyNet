package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.PhoneAlertResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des numéros de téléphone des personnes couvertes par une caserne spécifique.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneAlertService {
  private final FirestationService firestationService;

  /**
   * Récupère les numéros de téléphone des personnes couvertes par une caserne spécifique.
   *
   * @param stationNumber le numéro de la caserne
   * @return PhoneAlertResponseDTO contenant la liste des numéros de téléphone
   */
  public PhoneAlertResponseDTO findPhoneNumberOfPeopleByFirestation(int stationNumber) {
    List<Person> persons = firestationService.getPeopleByStation(stationNumber).getPersons();
    log.debug("Retrieved {} people covered by station {}", persons.size(), stationNumber);

    List<String> phoneNumber = persons.stream()
        .map(Person::getPhone)
        .distinct()
        .collect(Collectors.toList());

    log.debug("Found {} unique phone numbers for station {}", phoneNumber.size(), stationNumber);

    return new PhoneAlertResponseDTO(phoneNumber);
  }
}

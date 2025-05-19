package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FirestationService {

  private final DataRepository dataRepository;

  /**
   * Recherche la liste de personnes couvertes par un numéro de caserne donné
   */
  public FirestationResponseDTO getPeopleByStation(int stationNumber) {
    List<Person> persons = dataRepository.getPersons();
    List<Firestation> firestations = dataRepository.getFirestations();

    Set<String> addresses = firestations.stream()
        .filter(f -> f.getStation() == stationNumber)
        .map(Firestation::getAddress)
        .collect(Collectors.toSet());

    List<Person> peopleCoveredByStation = persons.stream()
        .filter(p -> addresses.contains(p.getAddress()))
        .collect(Collectors.toList());

    long adultsCount = peopleCoveredByStation.stream().filter(p -> p.getAge() >= 18).count();
    long childsCount = peopleCoveredByStation.size() - adultsCount;

    return new FirestationResponseDTO(peopleCoveredByStation, adultsCount, childsCount);
  }

  /**
   * Ajoute un objet Firestqtion avec une nouvelle adresse et un numéro de caserne.
   */
  public boolean addFirestation(String address, int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();

    if (firestations.stream()
        .anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      return false;
    } else {
      Firestation newFirestation = new Firestation(address, stationNumber);
      dataRepository.addFirestation(newFirestation);
      return true;
    }
  }

  /**
   * Mis à jour un numéro de caserne par une adresse.
   */
  public boolean setFirestation(String address, int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();

    if (firestations.stream().anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      dataRepository.updateFirestation(new Firestation(address, stationNumber));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Supprime une caserne sur la liste par une adresse associée.
   */
  public boolean deleteFirestationByAddress(String address) {
    List<Firestation> firestations = dataRepository.getFirestations();
    if (firestations.stream()
        .anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      dataRepository.deleteFirestationByAddress(address);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Supprime une caserne sur la liste par un numéro de la station associée.
   */
  public boolean deleteFirestationByStation(int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();
    if (firestations.stream()
        .anyMatch(f -> f.getStation() == stationNumber)) {
      dataRepository.deleteFirestationByStationNumber(stationNumber);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Lire une liste des casernes enregistrées.
   */
  public List<Firestation> getAllFirestations() {
    return dataRepository.getAllFirestations();
  }
}

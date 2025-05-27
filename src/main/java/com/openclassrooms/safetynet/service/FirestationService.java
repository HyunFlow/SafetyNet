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
   * Recherche la liste des personnes couvertes par une caserne donnée.
   *
   * @param stationNumber numéro de la caserne
   * @return une réponse contenant les personnes couvertes et un décompte des adultes et des enfants
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
   * Ajoute une nouvelle caserne avec une adresse et un numéro de station.
   *
   * @param address adresse de la caserne
   * @param stationNumber numéro de la station
   * @return true si l'ajout a réussi, false si une caserne existe déjà à cette adresse
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
   * Met à jour le numéro de station d’une caserne existante à une adresse donnée.
   *
   * @param address adresse de la caserne
   * @param stationNumber nouveau numéro de station
   * @return true si la mise à jour a réussi, false si l'adresse n'existe pas
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
   * Supprime une caserne en fonction de son adresse.
   *
   * @param address adresse de la caserne
   * @return true si la suppression a réussi, false si l'adresse n'existe pas
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
   * Supprime toutes les casernes associées à un numéro de station donné.
   *
   * @param stationNumber numéro de la station
   * @return true si la suppression a réussi, false si aucun enregistrement n'est trouvé
   */
  public boolean deleteFirestationByStation(int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();
    if (firestations.stream()
        .anyMatch(f -> f.getStation() == stationNumber)) {
      dataRepository.deleteFirestationByStation(stationNumber);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Recherche et retourne le numéro de station associé à une adresse donnée.
   *
   * @param address adresse recherchée
   * @return numéro de station, ou null si aucun résultat
   */
  public int getStationNumberByAddress(String address) {
    int stationNumber = dataRepository.getAllFirestations().stream()
        .filter(f -> f.getAddress().equalsIgnoreCase(address))
        .map(Firestation::getStation)
        .findFirst()
        .orElse(-1);
    return stationNumber;
  }

  /**
   * Retourne la liste de toutes les casernes enregistrées.
   *
   * @return liste des casernes
   */
  public List<Firestation> getAllFirestations() {
    return dataRepository.getAllFirestations();
  }
}

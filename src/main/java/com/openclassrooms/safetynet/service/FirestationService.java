package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.FirestationResponseDTO;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des casernes de pompiers.
 * Gère les opérations CRUD sur les casernes et fournit des fonctionnalités de recherche
 * pour les personnes couvertes par chaque caserne.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FirestationService {

  private final DataRepository dataRepository;

  /**
   * Recherche la liste des personnes couvertes par une caserne donnée.
   * Calcule également le nombre d'adultes et d'enfants dans la zone.
   *
   * @param stationNumber le numéro de la caserne
   * @return FirestationResponseDTO contenant la liste des personnes et les statistiques démographiques
   */
  public FirestationResponseDTO getPeopleByStation(int stationNumber) {
    List<Person> persons = dataRepository.getPersons();
    List<Firestation> firestations = dataRepository.getFirestations();

    Set<String> addresses = firestations.stream()
        .filter(f -> f.getStation() == stationNumber)
        .map(Firestation::getAddress)
        .collect(Collectors.toSet());

    log.debug("Found {} addresses covered by station {}", addresses.size(), stationNumber);

    List<Person> peopleCoveredByStation = persons.stream()
        .filter(p -> addresses.contains(p.getAddress()))
        .collect(Collectors.toList());

    long adultsCount = peopleCoveredByStation.stream().filter(p -> p.getAge() >= 18).count();
    long childsCount = peopleCoveredByStation.size() - adultsCount;

    return new FirestationResponseDTO(peopleCoveredByStation, adultsCount, childsCount);
  }

  /**
   * Ajoute une nouvelle caserne avec une adresse et un numéro de station.
   * Vérifie si l'adresse n'existe pas déjà avant l'ajout.
   *
   * @param address l'adresse de la nouvelle caserne
   * @param stationNumber le numéro de la station
   * @return true si l'ajout est réussi, false si l'adresse existe déjà
   */
  public boolean addFirestation(String address, int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();

    if (firestations.stream()
        .anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      log.debug("Failed to add firestation - address already exists: '{}'", address);
      return false;
    } else {
      Firestation newFirestation = new Firestation(address, stationNumber);
      dataRepository.addFirestation(newFirestation);
      log.debug("Successfully added new firestation: address='{}', station={}", address, stationNumber);
      return true;
    }
  }

  /**
   * Met à jour le numéro de station d'une caserne existante à une adresse donnée.
   * Vérifie si l'adresse existe avant la mise à jour.
   *
   * @param address l'adresse de la caserne à mettre à jour
   * @param stationNumber le nouveau numéro de station
   * @return true si la mise à jour est réussie, false si l'adresse n'existe pas
   */
  public boolean setFirestation(String address, int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();

    if (firestations.stream().anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      dataRepository.setFirestation(new Firestation(address, stationNumber));
      log.debug("Successfully updated firestation: address='{}', new station={}", address, stationNumber);
      return true;
    } else {
      log.debug("Failed to update firestation - address not found: '{}'", address);
      return false;
    }
  }

  /**
   * Supprime une caserne en fonction de son adresse.
   * Vérifie si l'adresse existe avant la suppression.
   *
   * @param address l'adresse de la caserne à supprimer
   * @return true si la suppression est réussie, false si l'adresse n'existe pas
   */
  public boolean deleteFirestationByAddress(String address) {
    List<Firestation> firestations = dataRepository.getFirestations();
    if (firestations.stream()
        .anyMatch(f -> f.getAddress().equalsIgnoreCase(address))) {
      dataRepository.deleteFirestationByAddress(address);
      log.debug("Successfully deleted firestation by address: '{}'", address);
      return true;
    } else {
      log.debug("Failed to delete firestation - address not found: '{}'", address);
      return false;
    }
  }

  /**
   * Supprime toutes les casernes associées à un numéro de station donné.
   * Vérifie si le numéro de station existe avant la suppression.
   *
   * @param stationNumber le numéro de station à supprimer
   * @return true si la suppression est réussie, false si le numéro n'existe pas
   */
  public boolean deleteFirestationByStation(int stationNumber) {
    List<Firestation> firestations = dataRepository.getFirestations();
    if (firestations.stream()
        .anyMatch(f -> f.getStation() == stationNumber)) {
      dataRepository.deleteFirestationByStation(stationNumber);
      log.debug("Successfully deleted firestations with station number: {}", stationNumber);
      return true;
    } else {
      log.debug("Failed to delete firestations - station number not found: {}", stationNumber);
      return false;
    }
  }

  /**
   * Recherche et retourne le numéro de station associé à une adresse donnée.
   *
   * @param address l'adresse à rechercher
   * @return le numéro de station, ou -1 si l'adresse n'est pas trouvée
   */
  public int getStationNumberByAddress(String address) {
    int stationNumber = dataRepository.getAllFirestations().stream()
        .filter(f -> f.getAddress().equalsIgnoreCase(address))
        .map(Firestation::getStation)
        .findFirst()
        .orElse(-1);
    
    if (stationNumber != -1) {
      log.debug("Found station number {} for address: '{}'", stationNumber, address);
    } else {
      log.debug("No station found for address: '{}'", address);
    }
    
    return stationNumber;
  }

  /**
   * Retourne la liste de toutes les casernes enregistrées.
   *
   * @return List<Firestation> contenant toutes les casernes
   */
  public List<Firestation> getAllFirestations() {
    List<Firestation> firestations = dataRepository.getAllFirestations();
    log.debug("Retrieved {} total firestations", firestations.size());
    return firestations;
  }
}

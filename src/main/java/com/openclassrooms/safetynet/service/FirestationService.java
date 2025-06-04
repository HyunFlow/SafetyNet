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

/**
 * Service pour la gestion des casernes de pompiers.
 * Gère les opérations CRUD sur les casernes et fournit des fonctionnalités de recherche
 * pour les personnes couvertes par chaque caserne.
 */
@Service
@RequiredArgsConstructor
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
      return false;
    } else {
      Firestation newFirestation = new Firestation(address, stationNumber);
      dataRepository.addFirestation(newFirestation);
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
      return true;
    } else {
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
      return true;
    } else {
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
      return true;
    } else {
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
    return stationNumber;
  }

  /**
   * Retourne la liste de toutes les casernes enregistrées.
   *
   * @return List<Firestation> contenant toutes les casernes
   */
  public List<Firestation> getAllFirestations() {
    return dataRepository.getAllFirestations();
  }
}

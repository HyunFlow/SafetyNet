package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.PersonDTO;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des personnes.
 * Gère les opérations CRUD sur les personnes et fournit des fonctionnalités
 * pour le calcul de l'âge et la gestion des informations personnelles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

  private final DataRepository dataRepository;

  /**
   * Calcule l'âge à partir d'une date de naissance au format MM/dd/yyyy.
   *
   * @param birthdate la date de naissance au format MM/dd/yyyy
   * @return l'âge calculé en années
   */
  public int calculateAge(String birthdate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate birthDate = LocalDate.parse(birthdate, formatter);
    return Period.between(birthDate, LocalDate.now()).getYears();
  }

  /**
   * Assigne l'âge à chaque personne de la liste en utilisant les dossiers médicaux correspondants.
   * Utilise une map pour optimiser la recherche des dossiers médicaux.
   *
   * @param persons la liste des personnes à mettre à jour
   * @param medicalRecords la liste des dossiers médicaux
   */
  public void assignAgesToPersons(List<Person> persons, List<MedicalRecord> medicalRecords) {
    Map<String, MedicalRecord> medicalRecordsMap = new HashMap<>();
    for (MedicalRecord medicalRecord : medicalRecords) {
      String key = medicalRecord.getFirstName() + medicalRecord.getLastName();
      medicalRecordsMap.put(key, medicalRecord);
    }

    for (Person person : persons) {
      String key = person.getFirstName() + person.getLastName();
      MedicalRecord medicalRecord = medicalRecordsMap.get(key);
      if (medicalRecord != null) {
        int age = calculateAge(medicalRecord.getBirthdate());
        person.setAge(age);
        log.debug("Age set to {}", age);
      }
    }
  }

  /**
   * Sauvegarde une nouvelle personne si elle n'existe pas déjà.
   * Vérifie l'existence de la personne avant l'ajout pour éviter les doublons.
   *
   * @param dto les informations de la personne à sauvegarder
   * @return true si la sauvegarde est réussie, false si la personne existe déjà
   */
  public boolean saveNewPerson(PersonDTO dto) {
    if (!personExists(dto)) {
      Person person = mapDtoToPerson(dto);
      return dataRepository.addPerson(person);
    } else {
      log.error("Person already exists");
      return false;
    }
  }

  /**
   * Met à jour une personne existante.
   * Vérifie l'existence de la personne avant la mise à jour.
   *
   * @param dto les nouvelles informations de la personne
   * @return true si la mise à jour est réussie, false si la personne n'existe pas
   */
  public boolean updatePerson(PersonDTO dto) {
    if(personExists(dto)) {
      Person person = mapDtoToPerson(dto);
      return dataRepository.setPerson(person);
    } else {
      log.error("Person does not exist");
      return false;
    }
  }

  /**
   * Supprime une personne existante.
   * Vérifie l'existence de la personne avant la suppression.
   *
   * @param dto les informations de la personne à supprimer
   * @return true si la suppression est réussie, false si la personne n'existe pas
   */
  public boolean deletePerson(PersonDTO dto) {
    if(personExists(dto)) {
      Person person = mapDtoToPerson(dto);
      return dataRepository.deletePerson(person);
    } else {
      log.error("Person does not exist");
      return false;
    }
  }

  /**
   * Récupère la liste complète des personnes sous forme de DTO.
   * Convertit chaque entité Person en PersonDTO.
   *
   * @return List<PersonDTO> contenant toutes les personnes
   */
  public List<PersonDTO> findAllPersons() {
    return dataRepository.getPersons().stream()
        .map(p -> new PersonDTO(
            p.getFirstName(),
            p.getLastName(),
            p.getAddress(),
            p.getCity(),
            p.getZip(),
            p.getPhone(),
            p.getEmail(),
            p.getAge()
        )).collect(Collectors.toList());
  }

  /**
   * Vérifie si une personne existe dans la base selon son prénom et nom.
   * La comparaison est insensible à la casse.
   *
   * @param dto les informations de la personne à vérifier
   * @return true si la personne existe, false sinon
   */
  private boolean personExists(PersonDTO dto) {
    return dataRepository.getPersons().stream()
        .anyMatch(p -> p.getFirstName().equalsIgnoreCase(dto.getFirstName()) &&
            p.getLastName().equalsIgnoreCase(dto.getLastName()));
  }

  /**
   * Convertit un PersonDTO en entité Person.
   * Copie toutes les propriétés du DTO vers une nouvelle entité.
   *
   * @param dto le DTO à convertir
   * @return une nouvelle instance de Person avec les propriétés du DTO
   */
  private Person mapDtoToPerson(PersonDTO dto) {
    Person person = new Person();
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setAddress(dto.getAddress());
    person.setCity(dto.getCity());
    person.setZip(dto.getZip());
    person.setPhone(dto.getPhone());
    person.setEmail(dto.getEmail());
    person.setAge(dto.getAge());
    return person;
  }
}
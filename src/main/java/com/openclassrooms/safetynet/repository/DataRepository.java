package com.openclassrooms.safetynet.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openclassrooms.safetynet.dto.DataWrapper;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des données.
 * Charge les données depuis un fichier JSON et les stocke dans des listes.
 * Fournit des méthodes pour ajouter, mettre à jour et supprimer des données.
 */
@Repository
@Slf4j
@Getter
@Setter
public class DataRepository {

  private final String filePath = "src/main/resources/Data.json";

  private List<Person> persons;
  private List<Firestation> firestations;
  private List<MedicalRecord> medicalRecords;

  /**
   * Charge les données depuis un fichier JSON et les stocke dans des listes.
   */
  public void loadData() {
    Gson gson = new Gson();
    log.info("Loading data from file " + filePath);

    try (Reader reader = new FileReader(filePath)) {
      DataWrapper data = gson.fromJson(reader, DataWrapper.class);

      this.persons = data.getPersons();
      this.firestations = data.getFirestations();
      this.medicalRecords = data.getMedicalRecords();
      log.info("Data loaded successfully");

    } catch (IOException e) {
      e.printStackTrace();
      log.error("Error loading data", e);
    }
  }

  /**
   * Sauvegarde les données actuelles dans le fichier JSON.
   */
  private void saveData() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    DataWrapper data = new DataWrapper();
    data.setPersons(this.persons);
    data.setFirestations(this.firestations);
    data.setMedicalRecords(this.medicalRecords);

    try (Writer writer = new FileWriter(filePath)) {
      gson.toJson(data, writer);
      log.info("Data saved successfully to file");
    } catch (IOException e) {
      e.printStackTrace();
      log.error("Error saving data", e);
    }
  }

  /**
   * Ajoute une caserne de pompiers à la liste.
   *
   * @param firestation la caserne de pompiers à ajouter
   */
  public void addFirestation(Firestation firestation) {
    this.firestations.add(firestation);
    saveData();
  }

  /**
   * Met à jour une caserne de pompiers existante.
   *
   * @param firestation la caserne de pompiers à mettre à jour
   */
  public void setFirestation(Firestation firestation) {
    for (int i = 0; i < firestations.size(); i++) {
      if (this.firestations.get(i).getAddress().equalsIgnoreCase(firestation.getAddress())) {
        this.firestations.set(i, firestation);
        saveData();
        break;
      }
    }
  }

  /**
   * Supprime une caserne de pompiers par son adresse.
   *
   * @param address l'adresse de la caserne à supprimer
   */
  public void deleteFirestationByAddress(String address) {
    boolean removed = firestations.removeIf(f -> f.getAddress().equalsIgnoreCase(address));
    if (removed) {
      saveData();
    }
  }

  /**
   * Supprime une caserne de pompiers par son numéro de station.
   *
   * @param stationNumber le numéro de station de la caserne à supprimer
   */
  public void deleteFirestationByStation(int stationNumber) {
    boolean removed = firestations.removeIf(f -> f.getStation() == stationNumber);
    if (removed) {
      saveData();
    }
  }

  /**
   * Récupère toutes les casernes de pompiers.
   *
   * @return List<Firestation> contenant toutes les casernes de pompiers
   */
  public List<Firestation> getAllFirestations() {
    return this.firestations;
  }

  /**
   * Ajoute une personne à la liste.
   *
   * @param newPerson la personne à ajouter
   * @return true si l'ajout est réussi, false sinon
   */
  public boolean addPerson(Person newPerson) {
    boolean added = this.persons.add(newPerson);
    if (added) {
      saveData();
    }
    return added;
  }

  /**
   * Met à jour une personne existante.
   *
   * @param updatedPerson la personne à mettre à jour
   * @return true si la mise à jour est réussie, false sinon
   */
  public boolean setPerson(Person updatedPerson) {
    for (int i = 0; i < persons.size(); i++) {
      Person existingPerson = this.persons.get(i);
      if (existingPerson.getFirstName().equalsIgnoreCase(updatedPerson.getFirstName())
          && existingPerson.getLastName().equalsIgnoreCase(updatedPerson.getLastName())) {
        this.persons.set(i, updatedPerson);
        saveData();
        return true;
      }
    }
    return false;
  }

  /**
   * Supprime une personne de la liste.
   *
   * @param existPerson la personne à supprimer
   * @return true si la suppression est réussie, false sinon
   */
  public boolean deletePerson(Person existPerson) {
    boolean removed = this.persons.removeIf(p -> p.getFirstName().equalsIgnoreCase(existPerson.getFirstName())
        && p.getLastName().equalsIgnoreCase(existPerson.getLastName()));
    if (removed) {
      saveData();
    }
    return removed;
  }

  /**
   * Ajoute un dossier médical à la liste.
   *
   * @param newMedicalRecord le dossier médical à ajouter
   * @return true si l'ajout est réussi, false sinon
   */
  public boolean addMedicalRecord(MedicalRecord newMedicalRecord) {
    boolean added = this.medicalRecords.add(newMedicalRecord);
    if (added) {
      saveData();
    }
    return added;
  }

  /**
   * Met à jour un dossier médical existant.
   *
   * @param updatedRecord le dossier médical à mettre à jour
   * @return true si la mise à jour est réussie, false sinon
   */
  public boolean setMedicalRecord(MedicalRecord updatedRecord) {
    for (int i = 0; i < this.medicalRecords.size(); i++) {
      MedicalRecord existingMedicalRecord = this.medicalRecords.get(i);
      if (existingMedicalRecord.getFirstName().equalsIgnoreCase(updatedRecord.getFirstName()) &&
          existingMedicalRecord.getLastName().equalsIgnoreCase(updatedRecord.getLastName())) {
        this.medicalRecords.set(i, updatedRecord);
        saveData();
        return true;
      }
    }
    return false;
  }

  /**
   * Supprime un dossier médical de la liste.
   *
   * @param existMedicalRecord le dossier médical à supprimer
   * @return true si la suppression est réussie, false sinon
   */
  public boolean deleteMedicalRecord(MedicalRecord existMedicalRecord) {
    boolean removed = this.medicalRecords.removeIf(mr -> mr.getFirstName().equalsIgnoreCase(existMedicalRecord.getFirstName())
        && mr.getLastName().equalsIgnoreCase(existMedicalRecord.getLastName()));
    if (removed) {
      saveData();
    }
    return removed;
  }
}

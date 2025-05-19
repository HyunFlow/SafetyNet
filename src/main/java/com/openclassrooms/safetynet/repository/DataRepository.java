package com.openclassrooms.safetynet.repository;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.dto.DataWrapper;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@Getter
public class DataRepository {

  private final String filePath = "src/main/resources/Data.json";

  private List<Person> persons;
  private List<Firestation> firestations;
  private List<MedicalRecord> medicalRecords;

  public void loadData() {
    Gson gson = new Gson();
    log.info("Loading data from file " + filePath);

    try (Reader reader = new FileReader(filePath)) {
      DataWrapper data = gson.fromJson(reader, DataWrapper.class);

      this.persons = data.getPersons();
      this.firestations = data.getFirestations();
      this.medicalRecords = data.getMedicalRecords();
      log.info("Data loaded successfully");

      for (Person person : persons) {
        System.out.println(person);
      }
      for (Firestation firestation : firestations) {
        System.out.println(firestation);
      }
      for (MedicalRecord medicalRecord : medicalRecords) {
        System.out.println(medicalRecord);
      }

    } catch (IOException e) {
      e.printStackTrace();
      log.error("Error loading data", e);
    }
  }

  public void addFirestation(Firestation firestation) {
    this.firestations.add(firestation);
  }

  public void updateFirestation(Firestation firestation) {
    for (int i = 0; i < firestations.size(); i++) {
      if (this.firestations.get(i).getAddress().equalsIgnoreCase(firestation.getAddress())) {
        this.firestations.set(i, firestation);
      }
    }
  }

  public void deleteFirestationByAddress(String address) {
    firestations.removeIf(f -> f.getAddress().equalsIgnoreCase(address));
  }

  public void deleteFirestationByStationNumber(int stationNumber) {
    firestations.removeIf(f -> f.getStation() == stationNumber);
  }

  public List<Firestation> getAllFirestations() {
    return this.firestations;
  }
}

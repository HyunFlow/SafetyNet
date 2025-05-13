package com.openclassrooms.safetynet.Repository;

import com.google.gson.Gson;
import com.openclassrooms.safetynet.Data.DataWrapper;
import com.openclassrooms.safetynet.Model.Firestation;
import com.openclassrooms.safetynet.Model.MedicalRecord;
import com.openclassrooms.safetynet.Model.Person;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    try(Reader reader = new FileReader(filePath)) {
      DataWrapper data = gson.fromJson(reader, DataWrapper.class);

      this.persons = data.getPersons();
      this.firestations = data.getFirestations();
      this.medicalRecords = data.getMedicalRecords();
      log.info("Data loaded successfully");

      for (Person person : data.getPersons()) {
        System.out.println(person);
      }
      for (Firestation firestation : data.getFirestations()) {
        System.out.println(firestation);
      }
      for (MedicalRecord medicalRecord : data.getMedicalRecords()) {
        System.out.println(medicalRecord);
      }
    } catch (IOException e) {
      e.printStackTrace();
      log.error("Error loading data", e);
    }
  }

  public List<String> findAddressesByStation(int stationNumber) {
    return firestations.stream().filter(f-> f.getStation() == stationNumber).map(Firestation::getAddress).collect(
        Collectors.toList());
  }


  public List<Person> findPeopleByStation(int stationNumber) {
    Set<String> addressesByStation = firestations.stream()
        .filter(f -> f.getStation() == stationNumber)
        .map(Firestation::getAddress)
        .collect(Collectors.toSet());

    return persons.stream()
        .filter(p -> addressesByStation.contains(p.getAddress()))
            .collect(Collectors.toList());
  }

}

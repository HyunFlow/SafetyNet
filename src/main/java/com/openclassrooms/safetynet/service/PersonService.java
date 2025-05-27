package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonService {

  /**
   * Calcule l'âge à partir de la date de naissance au format MM/dd/yyyy.
   */
  public int calculateAge(String birthdate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    LocalDate birthDate = LocalDate.parse(birthdate, formatter);
    return Period.between(birthDate, LocalDate.now()).getYears();
  }

  /**
   * Assigne l'âge à chaque objet Person en se basant sur les données des dossiers médicaux.
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

}

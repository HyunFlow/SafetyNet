package com.openclassrooms.safetynet.config;

import com.openclassrooms.safetynet.repository.DataRepository;
import com.openclassrooms.safetynet.service.PersonService;
import com.openclassrooms.safetynet.service.PhoneAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {
  private final DataRepository dataRepository;
  private final PersonService personService;
  private final PhoneAlertService phoneAlertService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    dataRepository.loadData();
    personService.assignAgesToPersons(dataRepository.getPersons(), dataRepository.getMedicalRecords());
  }
}

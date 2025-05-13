package com.openclassrooms.safetynet.Controller;

import com.openclassrooms.safetynet.Model.Person;
import com.openclassrooms.safetynet.Repository.DataRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirestationController {

  DataRepository dataRepository;

  @Autowired
  public FirestationController(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
    this.dataRepository.loadData();
  }

  @GetMapping("/firestation")
  public List<Person> getListOfPeopleByStationNumber(@RequestParam int stationNumber) {
    return dataRepository.findPeopleByStation(stationNumber);
  }
}

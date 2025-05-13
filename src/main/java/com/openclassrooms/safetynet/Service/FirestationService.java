package com.openclassrooms.safetynet.Service;

import com.openclassrooms.safetynet.Repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirestationService {

  @Autowired
  private DataRepository dataRepository;
  
  public FirestationService(DataRepository dataRepository) {
    this.dataRepository = dataRepository;
  }

}

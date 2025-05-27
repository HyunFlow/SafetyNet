package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.service.FireService;
import com.openclassrooms.safetynet.service.FirestationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FireController {

  private final FirestationService firestationService;
  private final FireService fireService;

  /**
   * Recherche les personnes vivant à une adresse donnée ainsi que le numéro de caserne associé.
   */
  @GetMapping("/fire")
  public FireResponseDTO getListOfPeopleByAddress(@RequestParam String address) {
    int stationNumber = firestationService.getStationNumberByAddress(address);

    if (stationNumber == -1) {
      log.warn("No firestation found for address {}", address);
    } else {
      log.info("Retrieving people covered by firestation address {}", address);
    }
    return fireService.findResidentsByAddress(address);
  }
}

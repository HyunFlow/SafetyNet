package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.service.FireService;
import com.openclassrooms.safetynet.service.FirestationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des informations liées aux incendies.
 * Fournit des endpoints pour obtenir des informations sur les casernes et les résidents en cas d'incendie.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FireController {

  private final FirestationService firestationService;
  private final FireService fireService;

  /**
   * Récupère les informations d'incendie (numéro de station et liste des résidents) pour une adresse donnée.
   *
   * @param address l'adresse à interroger
   * @return FireResponseDTO contenant le numéro de station et la liste des personnes vivant à l'adresse
   */
  @GetMapping("/fire")
  public FireResponseDTO getFireInfoByAddress(@RequestParam String address) {
    int stationNumber = firestationService.getStationNumberByAddress(address);

    if (stationNumber == -1) {
      log.error("No firestation found for address {}", address);
    } else {
      log.info("Retrieving people covered by firestation address {}", address);
    }
    return fireService.findResidentsByAddress(address);
  }
}

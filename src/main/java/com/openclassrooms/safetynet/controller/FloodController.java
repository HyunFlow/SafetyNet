package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FloodResponseDTO;
import com.openclassrooms.safetynet.service.FloodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des alertes d'inondation.
 * Fournit des endpoints pour obtenir des informations sur les foyers desservis par les casernes en cas d'inondation.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FloodController {

  private final FloodService floodService;

  /**
   * Récupère la liste des foyers desservis par les casernes spécifiées.
   *
   * @param stations la liste des numéros de caserne
   * @return List<FloodResponseDTO> contenant les informations des foyers par caserne
   */
  @GetMapping("/flood/stations")
  public List<FloodResponseDTO> getFloodStations(@RequestParam List<Integer> stations) {
    log.info("Retrieving flood information for stations {}", stations);
    return floodService.findHouseholdsByStationNumbers(stations);
  }
}

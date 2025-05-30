package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.FloodResponseDTO;
import com.openclassrooms.safetynet.service.FloodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FloodController {
  private final FloodService floodService;

  /**
   * Récupère les foyers desservis par une ou plusieurs casernes.
   *
   * @param stationNumbers Liste des numéros de casernes à interroger.
   * @return Une liste d’objets {@link FloodResponseDTO} contenant les adresses et leurs résidents.
   */
  @GetMapping("/flood/stations")
  public List<FloodResponseDTO> getHouseholdsByStations(@RequestParam List<Integer>stationNumbers) {
    log.info("Retrieving flood station numbers for {}", stationNumbers);
    return floodService.findHouseholdsByStationNumbers(stationNumbers);
  }
}

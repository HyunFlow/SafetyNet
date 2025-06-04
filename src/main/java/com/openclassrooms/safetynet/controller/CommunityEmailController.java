package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
import com.openclassrooms.safetynet.service.CommunityEmailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST pour la gestion des emails communautaires.
 * Fournit des endpoints pour récupérer les adresses email des résidents d'une ville.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommunityEmailController {

  private final CommunityEmailService communityEmailService;

  /**
   * Récupère toutes les adresses email des habitants d'une ville donnée.
   *
   * @param city le nom de la ville
   * @return List<CommunityEmailDTO> contenant la liste des adresses email des habitants
   */
  @GetMapping("/communityEmail")
  public List<CommunityEmailDTO> findCommunityEmail(@RequestParam String city) {
    log.info("Retrieving community email for city {}", city);
    return communityEmailService.findEmailByCity(city);
  }
}

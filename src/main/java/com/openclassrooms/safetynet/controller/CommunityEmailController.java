package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
import com.openclassrooms.safetynet.service.CommunityEmailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommunityEmailController {

  private final CommunityEmailService communityEmailService;

  /**
   * Récupère la liste des emails associés à une ville donnée
   */
  @GetMapping("/communityEmail")
  public List<CommunityEmailDTO> getCommunityEmail(@RequestParam String city) {
    log.info("Retrieving community email for city {}", city);
    return communityEmailService.findEmailByCity(city);
  }
}

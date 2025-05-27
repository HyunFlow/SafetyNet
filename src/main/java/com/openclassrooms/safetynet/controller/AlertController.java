package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.ChildAlertResponseDTO;
import com.openclassrooms.safetynet.dto.PhoneAlertResponseDTO;
import com.openclassrooms.safetynet.service.ChildAlertService;
import com.openclassrooms.safetynet.service.PhoneAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AlertController {

  private final ChildAlertService childAlertService;
  private final PhoneAlertService phoneAlertService;

  /**
   * Recherche les enfants vivant à une adresse donnée ainsi que les membres de leur foyer.
   */
  @GetMapping("/childAlert")
  public ChildAlertResponseDTO getListOfChildrenAndFamilyByAddress(@RequestParam String address) {
    return childAlertService.findChildrenAndFamilyByAddress(address);
  }

  /**
   * Recherche les numéros de téléphone des personnes couvertes par une caserne donnée.
   */
  @GetMapping("/phoneAlert")
  public PhoneAlertResponseDTO getListOfPhoneNumberByAddress(@RequestParam int stationNumber) {
    return phoneAlertService.findPhoneNumberOfPeopleByFirestation(stationNumber);
  }
}

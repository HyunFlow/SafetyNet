package com.openclassrooms.safetynet.controller;

import com.openclassrooms.safetynet.dto.ChildAlertResponseDTO;
import com.openclassrooms.safetynet.service.ChildAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChildAlertController {
  private final ChildAlertService childAlertService;

  @GetMapping("/childAlert")
  public ChildAlertResponseDTO getListOfChildrenAndFamilyByAddress(@RequestParam String address) {
    return childAlertService.findChildrenAndFamilyByAddress(address);
  }
}

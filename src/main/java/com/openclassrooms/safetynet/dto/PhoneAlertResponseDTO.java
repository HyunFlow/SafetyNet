package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de l'endpoint /phoneAlert.
 */
@Data
@AllArgsConstructor
public class PhoneAlertResponseDTO {
  private List<String> phoneNumber;
}

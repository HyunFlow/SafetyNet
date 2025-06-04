package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de l'endpoint /fire et /flood.
 */
@Data
@AllArgsConstructor
public class ResidentDTO {
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private int age;
  private List<String> medications;
  private List<String> allergies;
}

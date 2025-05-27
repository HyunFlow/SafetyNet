package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicalRecordResponseDTO {
  private String firstName;
  private String lastName;
  private String birthdate;
  private List<String> medications;
  private List<String> allergies;
}

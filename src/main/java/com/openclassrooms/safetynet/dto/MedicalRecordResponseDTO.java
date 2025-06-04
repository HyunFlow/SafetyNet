package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

/**
 * DTO pour la réponse de l'endpoint /medicalRecord.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponseDTO {
  @NotBlank(message = "First name is required")
  private String firstName;
  @NotBlank(message = "Last name is required")
  private String lastName;
  private String birthdate;
  private List<String> medications;
  private List<String> allergies;
}

package com.openclassrooms.safetynet.dto;

import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de l'endpoint /childAlert.
 */
@Data
@AllArgsConstructor
public class ChildAlertResponseDTO {
  private List<Person> children;
  private List<Person> family;
}

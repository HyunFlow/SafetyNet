package com.openclassrooms.safetynet.dto;

import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import lombok.Data;

@Data
public class ChildAlertResponseDTO {
  private List<Person> children;
  private List<Person> family;

  public ChildAlertResponseDTO (List<Person> children, List<Person> family) {
    this.children = children;
    this.family = family;
  }
}

package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonInfoResponseDTO {
  private String lastName;
  private String address;
  private int age;
  private String email;
  private List<String> medications;
  private List<String> allergies;
}

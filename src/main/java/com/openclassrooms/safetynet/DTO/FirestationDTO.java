package com.openclassrooms.safetynet.DTO;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class FirestationDTO {
  private List<PersonDTO> persons = new ArrayList<>();
  private int adultCount;
  private int childCount;
}

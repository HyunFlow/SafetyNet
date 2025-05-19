package com.openclassrooms.safetynet.dto;

import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirestationResponseDTO {

  private List<Person> persons;
  private long adultCount;
  private long childCount;
}

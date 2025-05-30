package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FloodResponseDTO {
  private int stationNumber;
  private String address;
  private List<ResidentDTO> residents;
}

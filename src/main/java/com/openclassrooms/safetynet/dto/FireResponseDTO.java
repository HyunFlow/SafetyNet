package com.openclassrooms.safetynet.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de l'endpoint /fire.
 */
@Data
@AllArgsConstructor
public class FireResponseDTO {
  private int stationNumber;
  private List<ResidentDTO> residents;
}

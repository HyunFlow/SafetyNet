package com.openclassrooms.safetynet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour la réponse de l'endpoint /communityEmail.
 */
@Data
@AllArgsConstructor
public class CommunityEmailDTO {

  private String email;
}

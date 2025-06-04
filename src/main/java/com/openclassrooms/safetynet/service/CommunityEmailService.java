package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des adresses électroniques des personnes résidant dans une ville spécifique.
 */
@Service
@RequiredArgsConstructor
public class CommunityEmailService {
  private final DataRepository dataRepository;

  /**
   * Récupère les adresses électroniques des personnes résidant dans une ville spécifique.
   *
   * @param city la ville à rechercher
   * @return List<CommunityEmailDTO> contenant les adresses électroniques des personnes
   */
  public List<CommunityEmailDTO> findEmailByCity(String city) {
    return dataRepository.getPersons().stream()
        .filter(p -> p.getCity().equalsIgnoreCase(city))
        .map(p -> new CommunityEmailDTO(p.getEmail())
        ).collect(Collectors.toList());
  }
}

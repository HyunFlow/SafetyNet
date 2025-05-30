package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.CommunityEmailDTO;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommunityEmailService {
  private final DataRepository dataRepository;

  public List<CommunityEmailDTO> findEmailByCity(String city) {
    return dataRepository.getPersons().stream()
        .filter(p -> p.getCity().equalsIgnoreCase(city))
        .map(p -> new CommunityEmailDTO(p.getEmail())
        ).collect(Collectors.toList());
  }
}

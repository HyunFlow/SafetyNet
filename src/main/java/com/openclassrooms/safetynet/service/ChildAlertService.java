package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.ChildAlertResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des enfants et des membres adultes de leurs familles résidant à une adresse spécifique.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChildAlertService {

  private final DataRepository dataRepository;

  /**
   * Retourne la liste des enfants (âgés de 18 ans ou moins) vivant à l'adresse donnée,
   * ainsi que la liste des membres adultes de leurs familles résidant à la même adresse.
   *
   * @param address l'adresse à rechercher
   * @return ChildAlertResponseDTO contenant la liste des enfants et des membres adultes
   */
  public ChildAlertResponseDTO findChildrenAndFamilyByAddress(String address) {
    List<Person> persons = dataRepository.getPersons();

    List<Person> childrenByAddress = persons.stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address) && p.getAge() <= 18)
        .collect(Collectors.toList());

    log.debug("Found {} children at address: '{}'", childrenByAddress.size(), address);

    Set<String> lastNamesOfChildren = childrenByAddress.stream()
        .map(Person::getLastName)
        .collect(Collectors.toSet());

    log.debug("Found {} unique family names among children", lastNamesOfChildren.size());

    List<Person> familyMembers = persons.stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address) && lastNamesOfChildren.contains(p.getLastName()) && p.getAge() > 18)
        .collect(Collectors.toList());

    log.debug("Found {} adult family members at address: '{}'", familyMembers.size(), address);

    return new ChildAlertResponseDTO(childrenByAddress, familyMembers);
  }
}

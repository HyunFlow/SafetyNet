package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.ChildAlertResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildAlertService {

  /**
   * 매개변수로 입력되는 주소로 미성년자 리스트 반환
   */
  private final DataRepository dataRepository;

  public ChildAlertResponseDTO findChildrenAndFamilyByAddress(String address) {
    List<Person> persons = dataRepository.getPersons();

    List<Person> childrenByAddress = dataRepository.getPersons().stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address) && p.getAge() <= 18)
        .collect(Collectors.toList());

    Set<String> lastNamesOfChildren = childrenByAddress.stream()
        .map(Person::getLastName)
        .collect(Collectors.toSet());

    List<Person> familyMembers = persons.stream()
        .filter(p -> p.getAddress().equalsIgnoreCase(address) && lastNamesOfChildren.contains(p.getLastName()) && p.getAge() > 18)
                .collect(Collectors.toList());

    return new ChildAlertResponseDTO(childrenByAddress, familyMembers);
  }
}

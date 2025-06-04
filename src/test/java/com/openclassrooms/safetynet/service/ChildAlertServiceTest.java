package com.openclassrooms.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.openclassrooms.safetynet.dto.ChildAlertResponseDTO;
import com.openclassrooms.safetynet.model.Person;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChildAlertServiceTest {
  @Mock
  private DataRepository dataRepository;
  @InjectMocks
  private ChildAlertService childAlertService;

  @Test
  public void findChildrenAndFamilyByAddress_shouldReturnChildAlertDto() {
    // given
    String inputAddress = "1509 Culver St";
    List<Person> persons = List.of(
        new Person("John", "Boyd", "1509 Culver St", null, null, null, null, 41),
        new Person("Jacob", "Boyd", "1509 Culver St", null, null, null, null, 36),
        new Person("Roger","Boyd", "1509 Culver St", null, null, null, null, 8),
        new Person("Shawna", "Stelzer", "947 E. Rose Dr", null, null, null, null, 55)
    );

    Mockito.when(dataRepository.getPersons()).thenReturn(persons);

    // when
    ChildAlertResponseDTO result = childAlertService.findChildrenAndFamilyByAddress(inputAddress);

    // then
    List<Person> children = result.getChildren();
    List<Person> family = result.getFamily();

    assertThat(children).hasSize(1);
    assertThat(children.get(0).getFirstName()).isEqualTo("Roger");

    assertThat(family).hasSize(2);
    assertThat(family.get(0).getFirstName()).isEqualTo("John");

    Mockito.verify(dataRepository, Mockito.times(1)).getPersons();
  }
}

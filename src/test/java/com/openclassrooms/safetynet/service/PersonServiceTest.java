package com.openclassrooms.safetynet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import com.openclassrooms.safetynet.dto.PersonDTO;
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
public class PersonServiceTest {

  @Mock
  private DataRepository dataRepository;

  @InjectMocks
  private PersonService personService;

  @Test
  public void saveNewPerson_shouldReturnTrue_whenPersonDoesNotExist() {
    // given
    PersonDTO dto = new PersonDTO();

    // when
    Mockito.when(dataRepository.addPerson(any(Person.class))).thenReturn(true);

   // then
   boolean result = personService.saveNewPerson(dto);
   assertThat(result).isTrue();
   Mockito.verify(dataRepository).addPerson(any(Person.class));
  }

  @Test
  public void saveNewPerson_shouldReturnFalse_whenPersonExisting() {
    // given
    PersonDTO dto = new PersonDTO("John", "Boyd", null, null, null, null, null, 41);

    List<Person> existingPerson = List.of(new Person("John", "Boyd", null, null, null, null, null, 41));
    Mockito.when(dataRepository.getPersons()).thenReturn(existingPerson);

    // when
    boolean result = personService.saveNewPerson(dto);

    // then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).addPerson(any(Person.class));
  }

  @Test
  public void updatePerson_shouldReturnTrue_whenPersonExists() {
    // given
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");

    List<Person> existingPersons = List.of(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com", 41));

    // when
    Mockito.when(dataRepository.getPersons()).thenReturn(existingPersons);
    Mockito.when(dataRepository.setPerson(any(Person.class))).thenReturn(true);

    boolean result = personService.updatePerson(dto);

    //then
    assertThat(result).isTrue();
    Mockito.verify(dataRepository).setPerson(any(Person.class));
  }

  @Test
  public void updatePerson_shouldReturnFalse_whenPersonDoesNotExist() {
    // given
    PersonDTO dto = new PersonDTO();
    dto.setFirstName("John");
    dto.setLastName("Boyd");

    List<Person> existingPersons = List.of(new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com", 36));
    Mockito.when(dataRepository.getPersons()).thenReturn(existingPersons);

    // when
    boolean result = personService.updatePerson(dto);

    //then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).setPerson(any(Person.class));
  }

  @Test
  public void deletePerson_shouldReturnTrue_whenPersonExist() {
    // given
    PersonDTO dto = new PersonDTO("John", "Boyd", null, null, null, null, null, 41);

    List<Person> existingPersons = List.of(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com", 41));

    Mockito.when(dataRepository.getPersons()).thenReturn(existingPersons);
    Mockito.when(dataRepository.deletePerson(any(Person.class))).thenReturn(true);

    // when
    boolean result = personService.deletePerson(dto);

    // then
    assertThat(result).isTrue();
    Mockito.verify(dataRepository, Mockito.times(1)).deletePerson(any(Person.class));
  }

  @Test
  public void deletePerson_shouldReturnFalse_whenPersonDoesNotExist() {
    // given
    PersonDTO dto = new PersonDTO("John", "Boyd", null, null, null, null, null, 41);

    List<Person> existingPersons = List.of(new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com", 36));
    Mockito.when(dataRepository.getPersons()).thenReturn(existingPersons);

    // when
    boolean result = personService.deletePerson(dto);

    // then
    assertThat(result).isFalse();
    Mockito.verify(dataRepository, Mockito.never()).deletePerson(any(Person.class));
  }

  @Test
  public void findAllPersons_shouldReturnListOfPersonDto() {
    // given
    List<Person> persons = List.of(new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "jaboyd@email.com", 41),
        new Person("Jacob", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6513", "drk@email.com", 36));
    Mockito.when(dataRepository.getPersons()).thenReturn(persons);

    // when
    List<PersonDTO> result = personService.findAllPersons();

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getFirstName()).isEqualTo("John");
    assertThat(result.get(0).getEmail()).isEqualTo("jaboyd@email.com");
    assertThat(result.get(1).getFirstName()).isEqualTo("Jacob");
    assertThat(result.get(1).getEmail()).isEqualTo("drk@email.com");
    Mockito.verify(dataRepository).getPersons();
  }
}
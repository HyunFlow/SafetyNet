package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.dto.ResidentDTO;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FireService {

  private final DataRepository dataRepository;
  private final FirestationService firestationService;
  private final MedicalRecordService medicalRecordService;

  /**
   * Retourne les informations sur les habitants à une adresse spécifique,
   * ainsi que le numéro de la caserne de pompiers qui couvre cette adresse.
   */
  public FireResponseDTO findResidentsByAddress(String address) {
    int stationNumber = firestationService.getStationNumberByAddress(address);

    List<ResidentDTO> residents = dataRepository.getPersons().stream()
        .filter(r -> r.getAddress().equalsIgnoreCase(address))
        .map(r -> new ResidentDTO(
                r.getFirstName(),
                r.getLastName(),
                r.getPhone(),
                r.getAge(),
                medicalRecordService.findMedicationsByName(r.getFirstName(), r.getLastName()),
                medicalRecordService.findAllergiesByName(r.getFirstName(), r.getLastName())
            )
        ).collect(Collectors.toList());

    return new FireResponseDTO(stationNumber, residents);
  }
}

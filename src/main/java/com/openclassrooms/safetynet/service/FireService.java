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
        .filter(p -> p.getAddress().equalsIgnoreCase(address))
        .map(p -> new ResidentDTO(
                p.getFirstName(),
                p.getLastName(),
                p.getPhone(),
                p.getAge(),
                medicalRecordService.findMedicationsByName(p.getFirstName(), p.getLastName()),
                medicalRecordService.findAllergiesByName(p.getFirstName(), p.getLastName())
            )
        ).collect(Collectors.toList());

    return new FireResponseDTO(stationNumber, residents);
  }
}

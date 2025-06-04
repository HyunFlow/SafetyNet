package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.FireResponseDTO;
import com.openclassrooms.safetynet.dto.ResidentDTO;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des informations en cas d'incendie.
 * Fournit des fonctionnalités pour récupérer les informations des résidents
 * et de leur caserne de pompiers en cas d'incendie à une adresse donnée.
 */
@Service
@RequiredArgsConstructor
public class FireService {

  private final DataRepository dataRepository;
  private final FirestationService firestationService;
  private final MedicalRecordService medicalRecordService;

  /**
   * Retourne les informations sur les habitants à une adresse spécifique,
   * ainsi que le numéro de la caserne de pompiers qui couvre cette adresse.
   * Pour chaque résident, inclut le prénom, nom, numéro de téléphone,
   * âge, médicaments et allergies.
   *
   * @param address l'adresse à rechercher
   * @return FireResponseDTO contenant le numéro de la caserne et la liste des résidents
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

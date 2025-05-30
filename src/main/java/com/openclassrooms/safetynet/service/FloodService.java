package com.openclassrooms.safetynet.service;

import com.openclassrooms.safetynet.dto.FloodResponseDTO;
import com.openclassrooms.safetynet.dto.ResidentDTO;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.repository.DataRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FloodService {

  private final DataRepository dataRepository;
  private final MedicalRecordService medicalRecordService;

  /**
   * Recherche les foyers desservis par les casernes spécifiées.
   *
   * Pour chaque numéro de caserne fourni, cette méthode récupère toutes les adresses associées et
   * retourne une liste des foyers à ces adresses. Pour chaque résident, les informations suivantes
   * sont incluses : prénom, nom, numéro de téléphone, âge, médicaments et allergies.
   */
  public List<FloodResponseDTO> findHouseholdsByStationNumbers(List<Integer> stationNumbers) {

    Map<String, Integer> addressStationMap = dataRepository.getFirestations().stream()
        .filter(f -> stationNumbers.contains(f.getStation()))
        .collect(Collectors.toMap(
            Firestation::getAddress,
            Firestation::getStation,
            (existing, replacement) -> existing
        ));

    List<String> addresses = new ArrayList<>(addressStationMap.keySet());

    return addresses.stream()
        .map(address -> {
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

          int stationNumber = addressStationMap.get(address);

          return new FloodResponseDTO(stationNumber, address, residents);
        })
        .collect(Collectors.toList());
  }
}

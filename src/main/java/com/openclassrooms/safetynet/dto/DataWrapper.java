package com.openclassrooms.safetynet.dto;

import com.google.gson.annotations.SerializedName;
import com.openclassrooms.safetynet.model.Firestation;
import com.openclassrooms.safetynet.model.MedicalRecord;
import com.openclassrooms.safetynet.model.Person;
import java.util.List;
import lombok.Data;

/**
 * Json Data wrapper for the data.json file.
 */
@Data
public class DataWrapper {
    private List<Person> persons;
    private List<Firestation> firestations;
    @SerializedName("medicalrecords")
    private List<MedicalRecord> medicalRecords;
}

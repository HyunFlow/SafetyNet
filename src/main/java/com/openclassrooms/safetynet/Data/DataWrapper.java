package com.openclassrooms.safetynet.Data;

import com.google.gson.annotations.SerializedName;
import com.openclassrooms.safetynet.Model.Firestation;
import com.openclassrooms.safetynet.Model.MedicalRecord;
import com.openclassrooms.safetynet.Model.Person;
import java.util.List;
import lombok.Data;

@Data
public class DataWrapper {
    private List<Person> persons;
    private List<Firestation> firestations;
    @SerializedName("medicalrecords")
    private List<MedicalRecord> medicalRecords;
}

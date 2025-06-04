package com.openclassrooms.safetynet.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

/**
 * DTO pour la réponse de l'endpoint /person.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Address name is required")
    private String address;
    private String city;
    private String zip;
    private String phone;
    @Email(message = "Email should be valid")
    private String email;
    private int age;
}

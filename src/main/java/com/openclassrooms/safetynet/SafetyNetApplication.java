package com.openclassrooms.safetynet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SafetyNet Alerts Application is main class.
 *
 * This application provides an information management system for emergency services.
 *
 * This application provides the following main features:
 * - Emergency alert service
 * (Endpoint: GET /childAlert?address=<address>, GET /phoneAlert?firestation=<stationNumber>, GET /fire?address=<address>, GET /communityEmail?city=<city>, GET /flood?stations?stationNumber=<a list of stationNumber>)
 * - Person information management
 * (Endpoint: GET,POST,PUT,DELETE /person, GET /personInfolastName?lastName=<lastName>)
 * - Firestation information management
 * (Endpoint: GET,POST,PUT,DELETE /firestation, GET /firestation?stationNumber=<stationNumber>)
 * - Medical record management
 * (Endpoint: GET,POST,PUT,DELETE /medicalRecord)
 *
 */
@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyNetApplication.class, args);
    }

}

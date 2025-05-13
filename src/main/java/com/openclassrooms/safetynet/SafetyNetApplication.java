package com.openclassrooms.safetynet;

import com.openclassrooms.safetynet.Repository.DataRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SafetyNetApplication {

    public static void main(String[] args) {
        SpringApplication.run(SafetyNetApplication.class, args);

        DataRepository dataRepository = new DataRepository();
        dataRepository.loadData();
        System.out.println("---------------------");
        System.out.println(dataRepository.findAddressesByStation(4));
        System.out.println(dataRepository.findPeopleByStation(4));
    }

}

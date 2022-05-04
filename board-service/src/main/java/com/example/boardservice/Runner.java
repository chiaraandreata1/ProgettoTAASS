package com.example.boardservice;

import com.example.boardservice.rabbithole.FacilityRabbitClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(facilityRabbitClient.getHours());
    }
}

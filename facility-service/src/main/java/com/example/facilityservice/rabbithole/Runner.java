package com.example.facilityservice.rabbithole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private RabbitConfig.Client client;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 1; i < 10; i++) {
            client.ask();
        }
    }

}

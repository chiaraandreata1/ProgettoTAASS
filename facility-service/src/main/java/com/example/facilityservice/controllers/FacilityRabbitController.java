package com.example.facilityservice.controllers;


import com.example.facilityservice.models.Facility;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class FacilityRabbitController {

    @Autowired
    private Facility facility;

    @RabbitListener(queues =  "${rabbit.facility.hours.queue-name}")
    public Map<String, Integer> getHours() {
        System.out.println("ASKED");
        return facility.getHours();
    }
}

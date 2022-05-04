package com.example.facilityservice.controllers;


import com.example.facilityservice.models.Facility;
import com.example.shared.models.FacilityHours;
import com.example.shared.models.RabbitResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FacilityRabbitController {

    @Autowired
    private Facility facility;

    @RabbitListener(queues =  "${rabbit.facility.hours.queue-name}")
    public RabbitResponse<FacilityHours> getHours() {
        System.out.println("ASKED");

        return new RabbitResponse<>(new FacilityHours(facility.getOpeningTime(), facility.getClosingTime()));
    }
}

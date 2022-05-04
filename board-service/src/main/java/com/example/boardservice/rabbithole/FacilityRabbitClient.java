package com.example.boardservice.rabbithole;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FacilityRabbitClient {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FacilityRabbitProperties properties;

    public Map<String, Integer> getHours() {
        Map<String, Integer> response = (Map<String, Integer>)
                rabbitTemplate.convertSendAndReceive(properties.getExchangeName(), properties.getHours().getKey(), "");

        return response;
    }
}

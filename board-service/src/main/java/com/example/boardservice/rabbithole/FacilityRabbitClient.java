package com.example.boardservice.rabbithole;

import com.example.shared.models.FacilityHours;
import com.example.shared.rabbithole.RabbitResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FacilityRabbitClient {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FacilityRabbitProperties properties;

    public FacilityHours getHours() {
        RabbitResponse<FacilityHours> response = (RabbitResponse<FacilityHours>)
                rabbitTemplate.convertSendAndReceive(properties.getExchangeName(), properties.getHours().getKey(), "");

        RabbitResponse.check(response);

        return response.getBody();
    }
}

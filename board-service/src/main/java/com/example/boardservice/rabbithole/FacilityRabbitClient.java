package com.example.boardservice.rabbithole;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.RabbitResponse;
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

    public FacilityHours getHours() {
        RabbitResponse<FacilityHours> response = (RabbitResponse<FacilityHours>)
                rabbitTemplate.convertSendAndReceive(properties.getExchangeName(), properties.getHours().getKey(), "");

        if (response == null)
            throw new RuntimeException("Null response");
        else if (!response.isSuccess()) {
            throw new RuntimeException(String.format("%s: {%s}", response.getErrorType(), response.getErrorMessage()));
        }

        return response.getBody();
    }
}

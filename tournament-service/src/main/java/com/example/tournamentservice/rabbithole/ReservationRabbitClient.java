package com.example.tournamentservice.rabbithole;

import com.example.shared.rabbithole.RabbitTemplateWrapper;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationRabbitClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ReservationRabbitProperties properties;

    public ReservationResponse reserve(List<ReservationRequest> requests) {
        return new RabbitTemplateWrapper(rabbitTemplate)
                .send(properties.getExchangeName(), properties.getReserve(), requests);
    }
}

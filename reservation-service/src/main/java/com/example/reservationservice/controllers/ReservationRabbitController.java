package com.example.reservationservice.controllers;

import com.example.shared.rabbithole.RabbitResponse;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationRabbitController {

    @RabbitListener(queues = "${rabbit.reservation.reserve.queue-name}")
    public RabbitResponse<ReservationResponse> reservationRequest(ReservationRequest request) {

        // TODO scrivi sto metodo

        throw new UnsupportedOperationException();
    }
}

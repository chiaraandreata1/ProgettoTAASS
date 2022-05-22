package com.example.reservationservice.controllers;

import com.example.shared.rabbithole.RabbitRequest;
import com.example.shared.rabbithole.RabbitResponse;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationRabbitController {

    @RabbitListener(queues = "${rabbit.reservation.reserve.queue-name}")
    public RabbitResponse<ReservationResponse> reservationRequest(RabbitRequest<List<ReservationRequest>> request) {

        // TODO scrivi sto metodo

        // Dummy

        List<ReservationResponse.ReservationBinding> bindings = request.getRequestBody().stream()
                .map(rr -> new ReservationResponse.ReservationBinding(rr, -1L))
                .collect(Collectors.toList());

        return new RabbitResponse<>(new ReservationResponse(bindings));
    }

    @RabbitListener(queues = "${rabbit.reservation.delete.queue-name}")
    public RabbitResponse<Boolean> deletionRequest(List<Long> ids) {

        // TODO scrivi sto metodo

        // Dummy

        return new RabbitResponse<>(true);
    }
}

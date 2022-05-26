package com.example.reservationservice.controllers;

import com.example.shared.rabbithole.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
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
    public RabbitResponse<Boolean> deletionRequest(RabbitRequest<ReservationDeleteRequest> request) {

        ReservationDeleteRequest requestBody = request.getRequestBody();

        Long ownerID = requestBody.getOwnerID(); // TODO combacia con l'owner delle reservation
        List<Long> reservationIDs = requestBody.getReservationIDs(); // TOODO reservation da cancellare

        // TODO scrivi sto metodo
        RabbitResponse<Boolean> response;

        try {
            response = new RabbitResponse<>(true);
            throw new IllegalArgumentException();
        }  catch (Exception ex) {
            response = new RabbitResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        // Dummy


        return response;
    }
}

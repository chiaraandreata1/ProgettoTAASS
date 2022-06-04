package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.rabbithole.FacilityRabbitClient;
import com.example.reservationservice.repositories.ReservationRepository;
import com.example.shared.rabbithole.*;
import com.example.shared.tools.DateSerialization;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReservationRabbitController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @RabbitListener(queues = "${rabbit.reservation.reserve.queue-name}")
    public RabbitResponse<ReservationResponse> reservationRequest(RabbitRequest<List<ReservationRequest>> request) {
        List<Reservation> arrayReservations = new ArrayList<>();
        List<ReservationResponse.ReservationBinding> bindings = new ArrayList();

        try {
            for ( int i=0; i< request.getRequestBody().size(); i++)
            {
                Reservation reserv = new Reservation();
                reserv.setId(-1L);
                //ID OWNER
                reserv.setOwnerID(request.getRequestBody().get(i).getOwnerID());
                //TYPE RESERVATION
                reserv.setTypeReservation(request.getRequestBody().get(i).getOwnerType());
                //DATE
                Date date = DateSerialization.deserializeDateTime(request.getRequestBody().get(i).getDate());
                reserv.setDate(date);
                reserv.setnHours(request.getRequestBody().get(i).getHoursCount());
                reserv.setDate(date);
                //SPORT RESERVATION
                reserv.setSportReservation(new Long(2)); //TODO: manca lo sport info. Per ora è 2 cioè single tennis
                //COURT
                reserv.setCourtReserved(request.getRequestBody().get(i).getCourtID());

                if (reserv.getTypeReservation()==ReservationOwnerType.TOURNAMENT_MATCH) {
                    reservationController.checkReservation(reserv); //qui se c'è qualche bad request viene presa prima di creare tutte le reservations
                    arrayReservations.add(reserv);
                }
                else if (!reservationController.checkIntersection(reserv))
                    arrayReservations.add(reserv);
            }

            for (int i = 0; i<arrayReservations.size(); i++) {
                Reservation finalreserv = reservationRepository.save(arrayReservations.get(i));
                ReservationResponse.ReservationBinding binding = new ReservationResponse.ReservationBinding(request.getRequestBody().get(i), finalreserv.getId());
                bindings.add(binding);
            }

            /*
            List<ReservationResponse.ReservationBinding> bindings = request.getRequestBody().stream()
                    .map(rr -> new ReservationResponse.ReservationBinding(rr, -1L))
                    .collect(Collectors.toList());
             */
            return new RabbitResponse<>(new ReservationResponse(bindings.stream().collect(Collectors.toList())));
        }
        catch (Exception ex) {
            return new RabbitResponse<>(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @RabbitListener(queues = "${rabbit.reservation.delete.queue-name}")
    public RabbitResponse<Boolean> deletionRequest(RabbitRequest<ReservationDeleteRequest> request) {

        ReservationDeleteRequest requestBody = request.getRequestBody();

        Long ownerID = requestBody.getOwnerID();
        List<Long> reservationIDs = requestBody.getReservationIDs();

        //Controlli errori TODO: manca lo sportReservation (per ora è una long 2 cioè single tennis)
        for (Long id : reservationIDs) {
            Optional<Reservation> res = reservationRepository.findById(id);
            if (!res.isPresent())
                return new RabbitResponse<>(HttpStatus.NOT_FOUND, "Not Found a reservation");
            else if (!res.get().getOwnerID().equals(ownerID)) //devono essere tutte prenotazioni appartenenti a quell'owner che fa la richiesta
                return new RabbitResponse<>(HttpStatus.FORBIDDEN, "Delete not permitted by the logged user");
            else if (!res.get().getSportReservation().equals(new Long(2)) || !res.get().getTypeReservation().equals(requestBody.getOwnerType()))
                return new RabbitResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Some fields are not correct"); //controllo tipo sport e tipo prenotazione corretta
        }

        List<Reservation> allReservations = reservationRepository.findReservationsByIds(reservationIDs);
        for (Reservation res : allReservations){
            reservationRepository.deleteById(res.getId());
        }

        return new RabbitResponse<>(true);
        /*
        try {
            response = new RabbitResponse<>(true);
            throw new IllegalArgumentException();
        }  catch (Exception ex) {
            response = new RabbitResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }

        return response;
         */
    }
}

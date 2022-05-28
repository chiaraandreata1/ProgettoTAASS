package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Court;
import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.rabbithole.FacilityRabbitClient;
import com.example.reservationservice.repositories.ReservationRepository;
import com.example.shared.models.facility.CourtInfo;
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
                //date.setHours(10); //TODO: manca l'orario vero
                reserv.setDate(date);
                reserv.setnHours(request.getRequestBody().get(i).getHoursCount());
                reserv.setDate(date);
                //SPORT RESERVATION //TODO: manca anche lo sport del torneo
                reserv.setSportReservation(ReservationSportType.TENNIS);
                reservationController.checkReservation(reserv); //qui se c'è qualche bad request viene presa prima di creare tutte le reservations
                //COURT
                /*
                CourtInfo Court = CourtInfo.getCourts().get(request.getRequestBody().get(i).getCourtID().intValue()-1);
                reserv.setCourtReserved(Court.id);
                reserv.setSportReservation(Court.sport);
                 */
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

        //Controlli errori TODO: manca lo sport (per ora è tennis)
        for (Long id : reservationIDs) {
            Optional<Reservation> res = reservationRepository.findById(id);
            if (!res.isPresent())
                return new RabbitResponse<>(HttpStatus.NOT_FOUND, "Not Found a reservation");
            else if (!res.get().getOwnerID().equals(ownerID))
                return new RabbitResponse<>(HttpStatus.FORBIDDEN, "Delete not permitted by the logged user");
            else if (!res.get().getSportReservation().equals(ReservationSportType.TENNIS) || !res.get().getOwnerID().equals(requestBody.getOwnerID()) || !res.get().getTypeReservation().equals(requestBody.getOwnerType()))
                return new RabbitResponse<>(HttpStatus.BAD_REQUEST, "Some fields are not correct");
        }

        List<Reservation> allReservations = reservationRepository.findReservationsByIds(reservationIDs);
        /*
        for (Reservation res : allReservations){
            if (res.getSportReservation()!="tennis" || res.getOwnerID()!=requestBody.getOwnerID() || res.getTypeReservation()!=requestBody.getOwnerType())
                return new RabbitResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Some fields are not correct");
        }
         */
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
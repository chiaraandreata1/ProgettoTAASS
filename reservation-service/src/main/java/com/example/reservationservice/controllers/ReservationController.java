package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.rabbithole.FacilityRabbitClient;
import com.example.reservationservice.repositories.ReservationRepository;
import com.example.shared.models.facility.CourtInfo;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.rabbithole.ReservationSportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shared.tools.DateSerialization;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @GetMapping("/")
    public List<Reservation> list(){
        return reservationRepository.findAll();
    }

    @GetMapping("/date/{date}/sport/{sport}")
    public List<Reservation> findByDateAndSportReservations(@PathVariable String date, @PathVariable Long sport) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append(date).append(" 09:00");
        endDateTime.append(date).append(" 21:00");
        try {
            return reservationRepository.findAllByDateBetweenAndSportReservation(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), sport);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    /*
    @GetMapping("/player/{player}/date/{date}/sport/{sport}")
    public List<Reservation> findByPlayerAndDateAndSport(@PathVariable String player, @PathVariable String date, @PathVariable String sport) {
        return reservationRepository.getAllByPlayer(player, date, sport);
    }
     */

    Reservation checkReservation (Reservation reservation){

        //CONTROLLO TIME
        if (reservation.getDate().getHours()+reservation.getnHours()>24 || reservation.getDate().getHours()<8)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time variables are not correct");
        //CONTROLLO COURT CORRETTO
        SportInfo sportInfo = facilityRabbitClient.getSportInfo(reservation.getSportReservation());
        List<Long> courtIDs = sportInfo.getCourtIDs();
        if (!courtIDs.contains(reservation.getCourtReserved()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sport Reservations and Sport Court are not the same");

        //CONTROLLO INTERSEZIONE CON ALTRE RESERVATIONS
        String dateReservation = DateSerialization.serializeDate(reservation.getDate()).replace('/', '-');
        List<Reservation> reservationsForDay = findByDateAndSportReservations(dateReservation, reservation.getSportReservation());
        List<Integer> hoursReservation = new ArrayList<>();
        for (Integer i = 0; i < reservation.getnHours(); i++) {
            Integer firstHour = reservation.getDate().getHours();
            hoursReservation.add(firstHour + i);
        }

        for (Reservation res : reservationsForDay) {
            List<Integer> hoursReservations_SameDate_Reserved = new ArrayList<>();
            Integer firstHour = res.getDate().getHours();
            for (Integer i = 0; i < res.getnHours(); i++)
                hoursReservations_SameDate_Reserved.add(firstHour + i);
            HashSet<Integer> set = new HashSet<>();
            set.addAll(hoursReservations_SameDate_Reserved);
            set.retainAll(hoursReservation);

            if (!set.isEmpty() && res.getCourtReserved().equals(reservation.getCourtReserved()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your reservation intersect with the other reservations of the same day. Someone play in that court");
        }
        reservation.setId(-1L);
        return reservation;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public Reservation create(@RequestBody Reservation reservation){
        try {
            reservation = checkReservation(reservation);
            return reservationRepository.save(reservation);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservations(@PathVariable("id") long id){
        System.out.println("Delete reservation with id = " + id + "...");

        reservationRepository.deleteById(id);

        return new ResponseEntity<>("Reservation deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllReservations(){
        System.out.println("Delete all reservations...");

        reservationRepository.deleteAll();

        return new ResponseEntity<>("All reservations have been deleted!", HttpStatus.OK);
    }
    /*
    @RequestMapping(value = "/listByIds", method = RequestMethod.GET)
    public List<Reservation> findByIds(@RequestParam Long[] Ids){
        return reservationRepository.findReservationsByIds(Ids);
    }
     */
}

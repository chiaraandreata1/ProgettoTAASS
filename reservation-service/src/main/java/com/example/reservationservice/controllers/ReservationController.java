package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.rabbithole.FacilityRabbitClient;
import com.example.reservationservice.repositories.ReservationRepository;
import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.shared.tools.DateSerialization;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    //FUNZIONI USATE SOLO SU POSTGRES--------------------------------------------------------------
    @GetMapping("/")
    public List<Reservation> list(){
        return reservationRepository.findAll();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAllReservations(){
        System.out.println("Delete all reservations...");

        reservationRepository.deleteAll();

        return new ResponseEntity<>("All reservations have been deleted!", HttpStatus.OK);
    }
    //---------------------------------------------------------------------------------------------

    @GetMapping("/date/{date}/sport/{sport}/hour/{hour}")
    public List<Reservation> findByDateAndSportAndHours(@PathVariable String date, @PathVariable Long sport, @PathVariable Integer hour) {
        SportInfo sportInfo = facilityRabbitClient.getSportInfo(sport);
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder dateString = new StringBuilder();
        dateString.append(date).append(" ").append(hour).append(":00");
        try {
            return reservationRepository.findAllByDateAndCourtReservedIn(DAY_TIME_DATE_FORMAT.parse(dateString.toString()), sportInfo.getCourtIDs());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/date/{date}/sport/{sport}")
    public List<Reservation> findByDateAndSport(@PathVariable String date, @PathVariable Long sport) {
        FacilityHours hours = facilityRabbitClient.getHours();
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append(date).append(" ").append(hours.getOpeningTime()).append(":00");
        endDateTime.append(date).append(" ").append(hours.getClosingTime()).append(":00");
        try {
            List<Long> courtIDs = facilityRabbitClient.getSportInfo(sport).getCourtIDs();
            List<Reservation> finalList = reservationRepository.findAllByDateBetweenAndCourtReservedIn(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), courtIDs);
            List<Reservation> reservationsByDateAndCourts = new ArrayList<>();
            for (Reservation res : finalList){
                if (courtIDs.contains(res.getCourtReserved())) {
                    reservationsByDateAndCourts.add(res);
                    if (res.getnHours()>1) {
                        for (int i = 1; i<res.getnHours(); i++){
                            Reservation fictitiousReserv = new Reservation();
                            Date resDate = res.getDate(); Date fictitiousDate = new Date();
                            fictitiousDate.setDate(resDate.getDate()); fictitiousDate.setMonth(resDate.getMonth()); fictitiousDate.setYear(resDate.getYear());
                            fictitiousDate.setHours(resDate.getHours()+i); fictitiousDate.setMinutes(0); fictitiousReserv.setDate(fictitiousDate);
                            fictitiousReserv.setTypeReservation(res.getTypeReservation()); fictitiousReserv.setSportReservation(res.getSportReservation());
                            fictitiousReserv.setOwnerID(res.getOwnerID()); fictitiousReserv.setCourtReserved(res.getCourtReserved()); fictitiousReserv.setPlayers(res.getPlayers());
                            fictitiousReserv.setnHours(1);
                            reservationsByDateAndCourts.add(fictitiousReserv);
                        }
                    }
                }
            }
            return reservationsByDateAndCourts;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/sport/{sport}/user/{userLoggedId}")
    public List<Reservation> findByPlayerAndDateAndSport(@PathVariable Long sport, @PathVariable Long userLoggedId) {
        SportInfo sportInfo = facilityRabbitClient.getSportInfo(sport);
        Date today = new Date(); Calendar c = Calendar.getInstance(); c.setTime(today); c.add(Calendar.WEEK_OF_MONTH,2);
        Date endDate = c.getTime();
        return reservationRepository.findAllByDateBetweenAndCourtReservedIn(today, endDate, sportInfo.getCourtIDs());
    }

    boolean checkIntersection(Reservation reservation){
        String dateReservation = DateSerialization.serializeDate(reservation.getDate()).replace('/', '-');
        List<Reservation> reservationsForDay = findByDateAndSport(dateReservation, reservation.getSportReservation());
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
                return true;
        }
        return false;
    }

    Reservation checkReservation (Reservation reservation){
        FacilityHours hours = facilityRabbitClient.getHours();
        //CONTROLLO TIME
        if (reservation.getDate().getHours()+reservation.getnHours()> hours.getClosingTime() || reservation.getDate().getHours()<hours.getOpeningTime())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time variables are not correct");
        //CONTROLLO COURT CORRETTO
        SportInfo sportInfo = facilityRabbitClient.getSportInfo(reservation.getSportReservation());
        List<Long> courtIDs = sportInfo.getCourtIDs();
        if (!courtIDs.contains(reservation.getCourtReserved()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This sport doesn't have this court");
        //CONTROLLO INTERSEZIONE CON ALTRE RESERVATIONS
        if (checkIntersection(reservation))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your reservation intersect with the other reservations of the same day. Someone play in that court");

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

    @RequestMapping(value = "/getByIds", method = RequestMethod.GET)
    public List<Reservation> findByIds(@RequestParam List<Long> Ids) {
        return reservationRepository.findReservationsByIds(Ids);
    }
}

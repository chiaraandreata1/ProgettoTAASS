package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.rabbithole.FacilityRabbitClient;
import com.example.reservationservice.repositories.ReservationRepository;
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

    @GetMapping("/")
    public List<Reservation> list(){
        return reservationRepository.findAll();
    }

    @GetMapping("/date/{date}/sport/{sport}") //TODO: fixare
    public List<Reservation> findByDateAndSportReservations(@PathVariable String date, @PathVariable Long sport) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append(date).append(" 08:00");
        endDateTime.append(date).append(" 21:00");
        try {
            return reservationRepository.findAllByDateBetweenAndSportReservation(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), sport);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/date/{date}/sport/{sport}/hour/{hour}") //TODO: fixare
    public List<Reservation> findByDateAndSportReservations(@PathVariable String date, @PathVariable Long sport, @PathVariable Integer hour) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder dateString = new StringBuilder();
        dateString.append(date).append(" ").append(hour).append(":00");
        try {
            return reservationRepository.findAllByDateAndSportReservation(DAY_TIME_DATE_FORMAT.parse(dateString.toString()), sport);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/date/{date}/isTennis/{isTennis}")
    public List<Reservation> findByDateAndSportIsTennis(@PathVariable String date, @PathVariable Boolean isTennis) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append(date).append(" 08:00");
        endDateTime.append(date).append(" 21:00");
        try {
            List<Reservation> finalList = new ArrayList<>(); Long idSport;
            if (isTennis){
                List<Reservation> reservationsByDateSingle = reservationRepository.findAllByDateBetweenAndSportReservation(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), new Long(2));
                List<Reservation> reservationsByDateDouble = reservationRepository.findAllByDateBetweenAndSportReservation(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), new Long(3));
                finalList = Stream.concat(reservationsByDateSingle.stream(), reservationsByDateDouble.stream()).collect(Collectors.toList()); idSport = new Long(2);
            }
            else {
                idSport = new Long(4);
                finalList = reservationRepository.findAllByDateBetweenAndSportReservation(DAY_TIME_DATE_FORMAT.parse(startDateTime.toString()), DAY_TIME_DATE_FORMAT.parse(endDateTime.toString()), idSport);

            }
            SportInfo sportInfo = facilityRabbitClient.getSportInfo(idSport);
            List<Long> courtIDs = sportInfo.getCourtIDs();
            List<Reservation> reservationsByDateAndCourts = new ArrayList<>();
            for (Reservation res : finalList){
                if (courtIDs.contains(res.getCourtReserved()))
                    reservationsByDateAndCourts.add(res);
            }
            return reservationsByDateAndCourts;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/isTennis/{isTennis}/user/{userLoggedId}")
    public List<Reservation> findByPlayerAndDateAndSport(@PathVariable boolean isTennis, @PathVariable Long userLoggedId) {
        Date today = new Date(); Calendar c = Calendar.getInstance(); c.setTime(today); c.add(Calendar.WEEK_OF_MONTH,1);
        Date endDate = c.getTime();
        List<Reservation> reservationsByDate = reservationRepository.findAllByDateBetweenAndOwnerID(today, endDate, userLoggedId);
        List<Reservation> finalList = new ArrayList<>();
        for (Reservation res : reservationsByDate){
            if (isTennis && (res.getSportReservation()==2 || res.getSportReservation()==3))
                finalList.add(res);
            else if (!isTennis)
                finalList.add(res);
        }
        return finalList;
    }

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

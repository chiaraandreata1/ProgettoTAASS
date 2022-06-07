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

    @GetMapping("/date/{date}/isTennis/{isTennis}/hour/{hour}")
    public List<Reservation> findByDateAndIstennisAndHours(@PathVariable String date, @PathVariable boolean isTennis, @PathVariable Integer hour) {
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder dateString = new StringBuilder();
        dateString.append(date).append(" ").append(hour).append(":00");
        try {
            if (isTennis) {
                List<Reservation> singleTennis = reservationRepository.findAllByDateAndSportReservation(DAY_TIME_DATE_FORMAT.parse(dateString.toString()), new Long(2));
                List<Reservation> doubleTennis = reservationRepository.findAllByDateAndSportReservation(DAY_TIME_DATE_FORMAT.parse(dateString.toString()), new Long(3));
                return Stream.concat(singleTennis.stream(), doubleTennis.stream()).collect(Collectors.toList());
            }
            else return reservationRepository.findAllByDateAndSportReservation(DAY_TIME_DATE_FORMAT.parse(dateString.toString()), new Long(4));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/date/{date}/isTennis/{isTennis}")
    public List<Reservation> findByDateAndSportIsTennis(@PathVariable String date, @PathVariable Boolean isTennis) {
        FacilityHours hours = facilityRabbitClient.getHours();
        SimpleDateFormat DAY_TIME_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        StringBuilder startDateTime = new StringBuilder(); StringBuilder endDateTime = new StringBuilder();
        startDateTime.append(date).append(" ").append(hours.getOpeningTime()).append(":00");
        endDateTime.append(date).append(" ").append(hours.getClosingTime()).append(":00");
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


    @GetMapping("/isTennis/{isTennis}/user/{userLoggedId}")
    public List<Reservation> findByPlayerAndDateAndSport(@PathVariable boolean isTennis, @PathVariable Long userLoggedId) {
        Date today = new Date(); Calendar c = Calendar.getInstance(); c.setTime(today); c.add(Calendar.WEEK_OF_MONTH,2);
        Date endDate = c.getTime();
        List<Reservation> reservationsByDate = reservationRepository.findAllByDateBetween(today, endDate);
        List<Reservation> finalList = new ArrayList<>();
        for (Reservation res : reservationsByDate){
            if (isTennis && (res.getSportReservation()==2 || res.getSportReservation()==3) && res.getPlayers().contains(userLoggedId))
                finalList.add(res);
            else if (!isTennis && res.getSportReservation()==4 && res.getPlayers().contains(userLoggedId))
                finalList.add(res);
        }
        return finalList;
    }

    boolean checkIntersection(Reservation reservation){
        String dateReservation = DateSerialization.serializeDate(reservation.getDate()).replace('/', '-');
        boolean isTennis = (reservation.getSportReservation() == 2 || reservation.getSportReservation() == 3) ? true : false;
        List<Reservation> reservationsForDay = findByDateAndSportIsTennis(dateReservation, isTennis);
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sport Reservations and Sport Court are not the same");
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

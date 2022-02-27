package com.example.reservationservice.controllers;

import com.example.reservationservice.models.Reservation;
import com.example.reservationservice.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1")
public class ReservationController {
    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/reservations")
    public List<Reservation> list(){
        return reservationRepository.findAll();
    }

    @GetMapping(value = "reservations/date/{date}/sport/{sport}")
    public List<Reservation> findByDateAndSportReservations(@PathVariable String date, @PathVariable String sport) {
        List<Reservation> reservations = reservationRepository.findAllByDateReservationAndSportReservation(date, sport);
        return reservations;
    }

    @PostMapping(value="/reservations/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody Reservation reservation){
        reservationRepository.save(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservations(@PathVariable("id") long id){
        System.out.println("Delete reservation with id = " + id + "...");

        reservationRepository.deleteById(id);

        return new ResponseEntity<>("Reservation deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/reservations/delete")
    public ResponseEntity<String> deleteAllReservations(){
        System.out.println("Delete all reservations...");

        reservationRepository.deleteAll();

        return new ResponseEntity<>("All reservations have been deleted!", HttpStatus.OK);
    }
}

package com.example.reservationservice.repositories;

import com.example.reservationservice.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByDateReservationAndSportReservation(String date, String sportReservation);
}

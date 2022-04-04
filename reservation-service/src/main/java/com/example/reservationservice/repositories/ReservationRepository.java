package com.example.reservationservice.repositories;

import com.example.reservationservice.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByDateReservationAndSportReservation(String date, String sportReservation);

    @Query("select p from Reservation p WHERE :player in elements(p.players)")
    List<Reservation> getAllByPlayer(@Param("player") String player);

}

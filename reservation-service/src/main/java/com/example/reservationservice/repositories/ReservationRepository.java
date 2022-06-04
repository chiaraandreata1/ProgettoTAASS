package com.example.reservationservice.repositories;

import com.example.reservationservice.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByDateAndSportReservation(Date startDateTime, Long sportReservation);

    List<Reservation> findAllByDateBetweenAndSportReservation(Date startDateTime, Date endDateTime, Long sportReservation);

    List<Reservation> findAllByDateBetweenAndOwnerID(Date startDateTime, Date endDateTime, Long ownerID);

    @Query("select p from Reservation p WHERE p.id IN (:Ids)")
    List<Reservation> findReservationsByIds(List<Long> Ids);

    @Query("select p from Reservation p WHERE (:player in elements(p.players) AND p.sportReservation=:sport)")
    List<Reservation> getAllByPlayer(@Param("player") Long player, @Param("sport") Long sport);

}

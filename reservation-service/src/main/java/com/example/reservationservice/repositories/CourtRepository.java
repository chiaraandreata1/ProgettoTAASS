package com.example.reservationservice.repositories;

import com.example.reservationservice.models.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourtRepository extends JpaRepository<Court, Long> {
}

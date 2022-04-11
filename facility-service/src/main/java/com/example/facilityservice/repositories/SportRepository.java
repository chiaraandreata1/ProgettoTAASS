package com.example.facilityservice.repositories;

import com.example.facilityservice.models.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
}

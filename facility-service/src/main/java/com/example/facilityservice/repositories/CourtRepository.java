package com.example.facilityservice.repositories;

import com.example.facilityservice.models.Court;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourtRepository extends JpaRepository<Court, Long> {

    List<Court> getCourtBySport_Id(Long sportID);

    Integer countCourtsBySport_Id(Long sportID);
}

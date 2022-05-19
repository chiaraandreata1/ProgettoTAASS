package com.example.facilityservice.business;

import com.example.facilityservice.models.Court;
import com.example.facilityservice.models.Sport;
import com.example.facilityservice.repositories.CourtRepository;
import com.example.facilityservice.repositories.SportRepository;
import com.example.shared.exception.MissingEntityException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class FacilityBusiness {

    @Autowired
    SportRepository sportRepository;

    @Autowired
    CourtRepository courtRepository;

    public boolean isSportForCourtValid(Long sportID, Long courtID) throws MissingEntityException {

        Sport sport = sportRepository.findById(sportID)
                .orElseThrow(() -> new MissingEntityException(Sport.class, sportID));

        Court court = courtRepository.findById(courtID)
                .orElseThrow(() -> new MissingEntityException(Court.class, courtID));

        boolean check = false;

        while (!check && sport != null) {
            check = sport.equals(court.getSport());
            sport = sport.getParent();
        }

        return check;
    }
}

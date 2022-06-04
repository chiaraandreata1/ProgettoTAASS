package com.example.facilityservice.controllers;


import com.example.facilityservice.models.Court;
import com.example.facilityservice.models.Facility;
import com.example.facilityservice.models.Sport;
import com.example.facilityservice.repositories.CourtRepository;
import com.example.facilityservice.repositories.SportRepository;
import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.rabbithole.RabbitRequest;
import com.example.shared.rabbithole.RabbitResponse;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FacilityRabbitController {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private Facility facility;

    @RabbitListener(queues = "${rabbit.facility.hours.queue-name}")
    public RabbitResponse<FacilityHours> getHours() {
        System.out.println("ASKED");

        return new RabbitResponse<>(new FacilityHours(facility.getOpeningTime(), facility.getClosingTime()));
    }

    @RabbitListener(queues = "${rabbit.facility.sports.queue-name}")
    public RabbitResponse<SportInfo> getSportInfo(RabbitRequest<Long> request) {

        RabbitResponse<SportInfo> res;

        Long id;
        Integer playersPerTeam;
        Sport sport;
        String sportName;
//        Integer courtCount;
        Optional<Sport> optSport;

        id = request.getRequestBody();
        optSport = sportRepository.findById(id);

        if (optSport.isPresent()) {
            sport = optSport.get();

            if (sport.getPlayersPerTeam() != null) {
                playersPerTeam = sport.getPlayersPerTeam();
                sportName = sport.getName();
                while (sport.getParent() != null)
                    sport = sport.getParent();
//                courtCount = courtRepository.countCourtsBySport_Id(id);
                res = new RabbitResponse<>(new SportInfo(
                        sportName,
                        playersPerTeam,
                        courtRepository.getCourtBySport_Id(sport.getId()).stream()
                                .map(Court::getId)
                                .collect(Collectors.toList())));
            } else
                res = RabbitResponse.badRequest("Referenced sport is not a leaf");

        } else
            res = RabbitResponse.notFound("Sport not found");

        return res;
    }
}

package com.example.tournamentservice.controllers;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import com.example.tournamentservice.models.*;
import com.example.tournamentservice.rabbithole.FacilityRabbitClient;
import com.example.tournamentservice.rabbithole.ReservationRabbitClient;
import com.example.tournamentservice.repositories.MatchesRepository;
import com.example.tournamentservice.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class TournamentRestController {

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @Autowired
    private ReservationRabbitClient reservationRabbitClient;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MatchesRepository matchesRepository;

    @PostMapping("management/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Tournament createTournament(@RequestBody TournamentDefinition tournamentDefinition) {

        SportInfo sportInfo = facilityRabbitClient.getSportInfo(tournamentDefinition.getSport());
        FacilityHours hours = facilityRabbitClient.getHours();

        List<TournamentRound> rounds = tournamentDefinition.buildRounds();

        int requiredDaysCount = tournamentDefinition.requiredDaysCount(rounds,
                sportInfo.getMinutesPerMatch(),
                hours);

        if (requiredDaysCount > tournamentDefinition.getDates().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough days");

        tournamentDefinition.computeDates(rounds, sportInfo, hours);

        Tournament tournament = new Tournament(tournamentDefinition, rounds);

        tournament = this.tournamentRepository.save(tournament);

        List<ReservationRequest> reservationRequests = rounds.stream()
                .flatMap(round -> round.getMatches()
                        .stream()
                        .map(match -> new ReservationRequest(DateSerialization.serializeDate(match.getDate()),
                                tournamentDefinition.getName(),
                                ReservationRequest.OwnerType.TOURNAMENT_MATCH,
                                match.getId())))
                .collect(Collectors.toList());

        ReservationResponse response = reservationRabbitClient.reserve(reservationRequests);

        if (response.isDone()) {
            Map<Long, Long> map = response.getBindings()
                    .stream()
                    .collect(Collectors.toMap(
                            b -> b.getRequest().getOwnerID(),
                            ReservationResponse.ReservationBinding::getReservationID
                    ));

            List<Match> collect = rounds.stream().map(TournamentRound::getMatches)
                    .flatMap(Collection::stream)
                    .peek(m -> m.setReservationID(map.get(m.getId())))
                    .collect(Collectors.toList());

            matchesRepository.saveAll(collect);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Requested dates not available: %s", response.getConflictingRequests()));
        }

        return tournament;
    }
}

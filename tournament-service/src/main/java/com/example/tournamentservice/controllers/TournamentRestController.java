package com.example.tournamentservice.controllers;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.models.users.UserDetails;
import com.example.shared.models.users.UserInfo;
import com.example.shared.models.users.UserType;
import com.example.shared.rabbithole.ReservationRequest;
import com.example.shared.rabbithole.ReservationResponse;
import com.example.shared.tools.CurrentUser;
import com.example.shared.tools.DateSerialization;
import com.example.tournamentservice.models.*;
import com.example.tournamentservice.rabbithole.FacilityRabbitClient;
import com.example.tournamentservice.rabbithole.ReservationRabbitClient;
import com.example.tournamentservice.rabbithole.UserRabbitClient;
import com.example.tournamentservice.repositories.MatchesRepository;
import com.example.tournamentservice.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class TournamentRestController {

    @Autowired
    private FacilityRabbitClient facilityRabbitClient;

    @Autowired
    private ReservationRabbitClient reservationRabbitClient;

    @Autowired
    private UserRabbitClient userRabbitClient;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private MatchesRepository matchesRepository;

    /*
    Internal utils
     */
    private Tournament getTournament(Long id) {
        return tournamentRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("Tournament with id %d not found", id)));
    }

    private Tournament getTournament(Long id, Tournament.Status status) {
        Tournament tournament = getTournament(id);

        if (!Objects.equals(tournament.getStatus(), status))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Unexpected tournament status %s, expected status %s",
                            tournament.getStatus(),
                            status));

        return tournament;
    }

    private Tournament getTournament(Long id, String owner) {
        Tournament tournament = getTournament(id);

        if (!Objects.equals(tournament.getOwner(), owner))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user is not the tournament owner");

        return tournament;
    }

    private Tournament getTournament(Long id, Tournament.Status status, String owner) {
        Tournament tournament = getTournament(id, status);

        if (!Objects.equals(tournament.getOwner(), owner))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user is not the tournament owner");

        return tournament;
    }

    private Tournament addTeam(Long id, List<Long> players) {

        Tournament tournament = getTournament(id, Tournament.Status.CONFIRMED);

        SportInfo sportInfo = facilityRabbitClient.getSportInfo(tournament.getSport());

        if (players.size() != sportInfo.getPlayerPerTeam())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Wrong team size: required %d, given %d",
                            sportInfo.getPlayerPerTeam(),
                            players.size()));

        if (tournament.getTeams().size() == tournament.getMaxTeamsNumber())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more team allowed to join this tournament");

        List<UserInfo> nonPlayers = userRabbitClient.getUsersInfo(players).stream()
                .filter(userInfo -> userInfo.getType() != UserType.PLAYER)
                .collect(Collectors.toList());

        if (!nonPlayers.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Following users are not players: %s", nonPlayers));

        List<Long> alreadyRegistered =
                tournament.getTeams().stream()
                        .map(Team::getPlayers)
                        .flatMap(Collection::stream)
                        .filter(players::contains)
                        .collect(Collectors.toList());

        if (!alreadyRegistered.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Following players' id already registered: %s", alreadyRegistered));
        }

        tournament.addTeam(new Team(players));
        return tournamentRepository.save(tournament);
    }

    private void complete(Tournament tournament) {
        List<Team> teams = tournament.getTeams();
        List<Match> deleted = new ArrayList<>();
        List<TournamentRound> rounds = new ArrayList<>(tournament.getRounds());

        TournamentRound round0;

        while (teams.size() * 2 < (round0 = rounds.get(0)).getMatches().size()) {
            matchesRepository.deleteAll(round0.getMatches());
            rounds.remove(round0);
            deleted.addAll(round0.getMatches());
        }

        Collections.shuffle(rounds);

        for (int i = 0; i < teams.size(); i += 2) {
            Match match = round0.getMatches().get(i / 2);
            match.setSide0(teams.get(i));

            if (i + 1 < teams.size())
                match.setSide1(teams.get(i + 1));
        }

        matchesRepository.saveAll(round0.getMatches());

        reservationRabbitClient.delete(deleted.stream().map(Match::getReservationID).collect(Collectors.toList()));
    }

    /*
    Controller's methods
     */

    @GetMapping("get")
    @ResponseStatus(HttpStatus.OK)
    public Tournament get(@RequestParam Long id) {
        return getTournament(id);
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Tournament createTournament(@RequestBody TournamentDefinition tournamentDefinition, @CurrentUser UserDetails userDetails) {

        if (tournamentDefinition.getSport() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing sport id");

        if (tournamentDefinition.getMaxTeamsNumber() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing max number of teams");

        if (tournamentDefinition.getDates() == null || tournamentDefinition.getDates().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing dates");

        if (tournamentDefinition.getCourtsCount() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing number of courts");

        SportInfo sportInfo = facilityRabbitClient.getSportInfo(tournamentDefinition.getSport());
        FacilityHours hours = facilityRabbitClient.getHours();

        List<TournamentRound> rounds = tournamentDefinition.buildRounds();

        int requiredDaysCount = tournamentDefinition.requiredDaysCount(rounds,
                sportInfo.getMinutesPerMatch(),
                hours);

        if (requiredDaysCount > tournamentDefinition.getDates().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough days");

        tournamentDefinition.computeDates(rounds, sportInfo, hours);

        Tournament tournament = new Tournament(tournamentDefinition, rounds, userDetails.getUsername());

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

    @GetMapping("confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament confirmTournament(@RequestParam Long id, @CurrentUser UserDetails userDetails) {

        Tournament tournament = getTournament(id, Tournament.Status.DRAFT, userDetails.getUsername());

        tournament.setStatus(Tournament.Status.CONFIRMED);

        return tournamentRepository.save(tournament);
    }

    @GetMapping("close-registrations")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament closeRegistrations(@RequestParam Long id, @CurrentUser UserDetails userDetails) {
        Tournament tournament = getTournament(id, Tournament.Status.CONFIRMED, userDetails.getUsername());

        if (tournament.getTeams().size() < 2) {
            tournament.setStatus(Tournament.Status.CANCELLED);
        } else {
            complete(tournament);
        }

        return tournamentRepository.save(tournament);
    }

    @GetMapping("cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament cancel(@RequestParam Long id, @CurrentUser UserDetails userDetails) {
        Tournament tournament = getTournament(id, userDetails.getUsername());

        if (Tournament.Status.CANCELLED.equals(tournament.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament already cancelled");

        if (Tournament.Status.DONE.equals(tournament.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tournament already done");

        tournament.setStatus(Tournament.Status.CANCELLED);

        return tournamentRepository.save(tournament);
    }

    @GetMapping("join")
    @PreAuthorize("hasRole('PLAYER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament joinTournament(@RequestParam Long id, @CurrentUser UserDetails userDetails, @RequestParam List<Long> players) {

        if (!players.contains(userDetails.getId())) {
            players = new ArrayList<>(players);
            players.add(userDetails.getId());
        }

        return addTeam(id, players);
    }

    @GetMapping("register-players")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament addParticipants(@RequestParam Long id, @RequestParam List<Long> players) {
        return addTeam(id, players);
    }
}

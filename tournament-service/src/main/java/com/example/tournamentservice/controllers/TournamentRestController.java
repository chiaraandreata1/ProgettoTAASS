package com.example.tournamentservice.controllers;

import com.example.shared.models.FacilityHours;
import com.example.shared.models.facility.SportInfo;
import com.example.shared.models.users.UserDetails;
import com.example.shared.models.users.UserInfo;
import com.example.shared.models.users.UserType;
import com.example.shared.rabbithole.ReservationOwnerType;
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
import org.apache.catalina.User;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private Tournament getTournament(Long id, Long owner) {
        Tournament tournament = getTournament(id);

        if (!Objects.equals(tournament.getOwner(), owner))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user is not the tournament owner");

        return tournament;
    }

    private Tournament getTournament(Long id, Tournament.Status status, Long owner) {
        Tournament tournament = getTournament(id, status);

        if (!Objects.equals(tournament.getOwner(), owner))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current user is not the tournament owner");

        return tournament;
    }

    private Tournament addTeam(Long id, List<Long> playerIDs) {

        Tournament tournament = getTournament(id, Tournament.Status.CONFIRMED);

        SportInfo sportInfo = facilityRabbitClient.getSportInfo(tournament.getSport());

        if (playerIDs.size() != sportInfo.getPlayerPerTeam())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Wrong team size: required %d, given %d",
                            sportInfo.getPlayerPerTeam(),
                            playerIDs.size()));

        if (tournament.getTeams().size() == tournament.getMaxTeamsNumber())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No more team allowed to join this tournament");

        List<UserInfo> players = userRabbitClient.getUsersInfo(playerIDs);



        List<UserInfo> nonPlayers = players.stream()
                .filter(userInfo -> userInfo.getType() != UserType.PLAYER)
                .collect(Collectors.toList());

        if (!nonPlayers.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Following users are not players: %s",
                            nonPlayers.stream().map(UserInfo::getEmail).collect(Collectors.toList())));

        Set<Long> registered = tournament.getTeams().stream()
                .map(Team::getPlayers)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        List<UserInfo> duplicate = players.stream().filter(ui -> registered.contains(ui.getId()))
                .collect(Collectors.toList());

        if (!duplicate.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Following players' already registered for this tournament: %s",
                            duplicate.stream().map(UserInfo::getEmail).collect(Collectors.toList())));
        }

        tournament.addTeam(new Team(playerIDs));
        return tournamentRepository.save(tournament);
    }

    private void complete(Tournament tournament) {

        List<Team> teams = tournament.getTeams();
        List<Long> toFree = new ArrayList<>();
//        List<Match> deleted = new ArrayList<>();
        int toKeep = (int) Math.ceil(teams.size() * 1. / 2);

        while (tournament.getRounds().size() > 1 && tournament.getRounds().get(1).size() >= toKeep) {
            toFree.addAll(tournament.getRounds().get(0).getMatches().stream().map(Match::getReservationID).collect(Collectors.toList()));
            tournament.getRounds().remove(0);
        }

        while (tournament.getRounds().get(0).size() > toKeep) {
            toFree.add(tournament.getRounds().get(0).getMatches().get(toKeep).getReservationID());
            tournament.getRounds().get(0).getMatches().remove(toKeep);
        }


        Collections.shuffle(teams);

        for (int i = 0; i < teams.size(); i += 2) {
            Match match = tournament.getRounds().get(0).getMatches().get(i / 2);
            match.setSide0(teams.get(i));

            if (i + 1 < teams.size())
                match.setSide1(teams.get(i + 1));
        }

        matchesRepository.saveAll(tournament.getRounds().get(0).getMatches());

        tournament.setStatus(Tournament.Status.COMPLETE);

        reservationRabbitClient.delete(toFree, tournament.getId());
    }

    /*
    Controller's methods
     */

//    @GetMapping("get")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasRole('USER')")
    public Tournament get(@RequestParam Long id, @CurrentUser UserDetails userDetails) {
        Tournament tournament = getTournament(id);

        LogFactory.getLog(TournamentRestController.class).error(RequestContextHolder.getRequestAttributes() != null ? RequestContextHolder.getRequestAttributes().getSessionId() : null);
        if (tournament.getStatus() == Tournament.Status.DRAFT
                && (userDetails == null || !Objects.equals(userDetails.getId(), tournament.getOwner())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Draft tournament, for owner only");

        return tournament;
    }

    @GetMapping("get")
//    @PreAuthorize("hasRole('USER')")
    public Tournament me(@RequestParam Long id, @CurrentUser UserDetails userDetails) {
        Tournament tournament = getTournament(id);

        if (tournament.getStatus() == Tournament.Status.DRAFT
                && (userDetails == null || !Objects.equals(userDetails.getId(), tournament.getOwner())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Draft tournament, for owner only");

        return tournament;
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

        int requiredDaysCount = tournamentDefinition.requiredDaysCount(rounds, hours);

        if (requiredDaysCount > tournamentDefinition.getDates().size())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough days");

        tournamentDefinition.computeDates(rounds, hours, sportInfo);

        Tournament tournament = new Tournament(tournamentDefinition, rounds, userDetails.getId());

        tournament = this.tournamentRepository.save(tournament);

        Long tournamentID = tournament.getId();

        List<ReservationRequest> reservationRequests = rounds.stream()
                .flatMap(round -> round.getMatches()
                        .stream()
                        .map(match -> new ReservationRequest(DateSerialization.serializeDate(match.getDate()),
                                ReservationOwnerType.TOURNAMENT_MATCH,
                                3,
                                match.getCourtID(), //TODO
                                tournamentID)))
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

        Tournament tournament = getTournament(id, Tournament.Status.DRAFT/*, userDetails.getId()*/);

        tournament.setStatus(Tournament.Status.CONFIRMED);

        return tournamentRepository.save(tournament);
    }

    @GetMapping("close-registrations")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ResponseStatus(HttpStatus.OK)
    public Tournament closeRegistrations(@RequestParam Long id, @CurrentUser UserDetails userDetails) {
        Tournament tournament = getTournament(id, Tournament.Status.CONFIRMED, userDetails.getId());

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
        Tournament tournament = getTournament(id/*, userDetails.getId()*/);

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

    private Team winner(Match match) {

        long p0 = match.getPoints0();
        long p1 = match.getPoints1();

        int a, b;
        a = b = 0;

        while (p0 > 0 || p1 > 0) {
            if (p0 % 10 > p1 % 10)
                a++;
            else if (p1 % 10 > p0 % 10)
                b++;

            p0 /= 10;
            p1 /= 10;
        }

        if (a > b)
            return match.getSide0();
        if (b > a)
            return match.getSide1();

        return null;
    }

    @GetMapping("match-results")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Tournament matchResults(@CurrentUser UserDetails userDetails,
                                   @RequestParam Long id,
                                   @RequestParam(name = "match") Long matchID,
                                   @RequestParam Long points0,
                                   @RequestParam Long points1) {

        Match match = matchesRepository.findById(matchID)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Match not found"));

        Tournament tournament = getTournament(id, Tournament.Status.COMPLETE);

        if (!Objects.equals(tournament.getRounds().get(match.getRound()).getMatches().get(match.getRoundHeight()).getId(), matchID))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Match and tournament not matching");

        tournament.getRounds().get(match.getRound()).getMatches().get(match.getRoundHeight()).setPoints0(points0);
        tournament.getRounds().get(match.getRound()).getMatches().get(match.getRoundHeight()).setPoints1(points1);

        int nextRound = match.getRound() + 1;

        if (nextRound < tournament.getRounds().size()) {
            int nextHeight = match.getRoundHeight() / 2;

            Match nextMatch = tournament.getRounds().get(nextRound).getMatches().get(nextHeight);
            Team winner = winner(match);

            boolean top = nextHeight * 2 == match.getRoundHeight();

            if (top)
                nextMatch.setSide0(winner);
            else
                nextMatch.setSide1(winner);

            if (top && match.getRoundHeight() == tournament.getRound(match.getRound()).size() - 1
                    || (top ? nextMatch.getSide1() : nextMatch.getSide0()) != null)
                nextMatch.setStatus(Match.Status.READY);
        } else {
            tournament.setStatus(Tournament.Status.DONE);
        }

        match.setStatus(Match.Status.DONE);

        return tournament;
    }

    private void subTest(Tournament tournament) {
        List<Team> teams = tournament.getTeams();
        List<Long> toFree = new ArrayList<>();
        int toKeep = (int) Math.ceil(teams.size() * 1. / 2);

        for (int i = 0, l = tournament.getRounds().size() - 1; i < l && tournament.getRound(i).size() > toKeep; i++) {

            if (tournament.getRound(i + 1).size() >= toKeep) {
                toFree.addAll(tournament.getRound(0).getMatches().stream().map(Match::getReservationID).collect(Collectors.toList()));
                tournament.getRounds().remove(0);
            } else {

                while (tournament.getRound(i).size() > toKeep) {
                    toFree.add(tournament.getRound(i).getMatch(0).getReservationID());
                    tournament.getRound(i).getMatches().remove(0);
                }

                for (int j = 0; j < toKeep; j++) {
                    tournament.getRound(i).getMatch(j).setRoundHeight(j);
                }

                toKeep = (int) Math.ceil(toKeep * 1. / 2);
            }
        }

        Collections.shuffle(teams);

        for (int i = 0; i < teams.size(); i += 2) {
            Match match = tournament.getRounds().get(0).getMatches().get(i / 2);
            match.setSide0(teams.get(i));

            if (i + 1 < teams.size())
                match.setSide1(teams.get(i + 1));

            match.setStatus(Match.Status.READY);
        }

        reservationRabbitClient.delete(toFree, tournament.getId());
        tournament.setStatus(Tournament.Status.COMPLETE);

    }

    @GetMapping("test")
    public Object test(@RequestParam Long id) {
        Tournament tournament = tournamentRepository.findById(id).orElse(null);

        subTest(tournament);

        tournamentRepository.save(tournament);

        return tournament;
    }

}

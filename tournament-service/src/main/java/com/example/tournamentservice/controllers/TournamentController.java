package com.example.tournamentservice.controllers;

import com.example.tournamentservice.models.Match;
import com.example.tournamentservice.models.Team;
import com.example.tournamentservice.models.Tournament;
import com.example.tournamentservice.models.TournamentBuilding;
import com.example.tournamentservice.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

    @PostMapping("test")
    public String test(@RequestBody Team[] teams) {
        StringBuilder builder = new StringBuilder();
        for (Team team : teams) {
            for (String player : team.getPlayers()) {
                builder.append(player).append(' ');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @PostMapping("create")
    @ResponseStatus(HttpStatus.OK)
    public Tournament createTournament(@RequestBody TournamentBuilding parameters) {

        return parameters.build();
    }

    @PostMapping("confirm")
    @ResponseStatus(HttpStatus.OK)
    public Tournament confirmTournament(@RequestBody Tournament tournament) {
        tournament.setId(-1L);
        tournament = this.tournamentRepository.save(tournament);
        return tournament;
    }

    @GetMapping("/{id}")
    public Tournament getTournament(@PathVariable Long id) {
        return tournamentRepository.getById(id);
    }
}

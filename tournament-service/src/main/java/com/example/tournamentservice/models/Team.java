package com.example.tournamentservice.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

public class Team {

    private List<Long> players;

    public Team() {
    }

    public Team(Long... players) {
        this(Arrays.asList(players));
    }

    public Team(List<Long> players) {
        this.players = players;
    }

    public List<Long> getPlayers() {
        return players;
    }

    public void setPlayers(List<Long> players) {
        this.players = players;
    }
}

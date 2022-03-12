package com.example.tournamentservice.models;

import javax.persistence.*;
import java.util.List;

public class Team {

    private List<String> players;

    public Team() {
    }

    public Team(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}

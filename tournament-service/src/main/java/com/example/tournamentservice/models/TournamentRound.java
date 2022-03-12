package com.example.tournamentservice.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rounds")
public class TournamentRound {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int round;
    @OneToMany
    private List<Match> matches;

    protected TournamentRound() {

    }

    public TournamentRound(int round, List<Match> matches) {
        this.round = round;
        this.matches = matches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public int size() {
        return matches.size();
    }
}

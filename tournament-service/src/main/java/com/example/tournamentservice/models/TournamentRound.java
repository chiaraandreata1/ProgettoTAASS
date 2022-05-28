package com.example.tournamentservice.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rounds")
public class TournamentRound {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private List<Match> matches;

    protected TournamentRound() {

    }

    public TournamentRound(List<Match> matches) {
        this.matches = matches;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public Match getMatch(int index) {
        return matches.get(index);
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public int size() {
        return matches.size();
    }
}

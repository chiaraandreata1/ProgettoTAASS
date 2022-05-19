package com.example.tournamentservice.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name="tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean confirmed;

    private String name;
    private String description;
    private Long sport;
    private Double price;
    private Double prize;
//    private String level;

    private Integer maxTeamsNumber;

    @Columns(columns = {@Column(name = "player1"), @Column(name = "player2")})
    @Type(type = "com.example.tournamentservice.models.TeamType")
    @ElementCollection
    private List<Team> participants;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TournamentRound> rounds;

    public Tournament() {}

    public Tournament(TournamentDefinition tournamentDefinition, List<TournamentRound> rounds) {

        this.confirmed = false;

        this.name = tournamentDefinition.getName();
        this.description = tournamentDefinition.getDescription();
        this.sport = tournamentDefinition.getSport();
        this.price = tournamentDefinition.getPrice();
        this.prize = tournamentDefinition.getPrize();
        this.maxTeamsNumber = tournamentDefinition.getMaxTeamsNumber();
        this.participants = new ArrayList<>();
        this.rounds = rounds;
    }

    public Tournament(String name,
                      String description,
                      Long sport,
                      Double price,
                      Double prize,
//                      String level,
                      List<TournamentRound> rounds) {
        this.name = name;
        this.description = description;
        this.sport = sport;
        this.price = price;
        this.prize = prize;
//        this.level = level;
        this.rounds = rounds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSport() {
        return sport;
    }

    public void setSport(Long sport) {
        this.sport = sport;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrize() {
        return prize;
    }

    public void setPrize(Double prize) {
        this.prize = prize;
    }

    public Integer getMaxTeamsNumber() {
        return maxTeamsNumber;
    }

    public void setMaxTeamsNumber(Integer maxTeamsNumber) {
        this.maxTeamsNumber = maxTeamsNumber;
    }

    public List<Team> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Team> participants) {
        this.participants = participants;
    }
/*public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }*/

    public List<TournamentRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<TournamentRound> rounds) {
        this.rounds = rounds;
    }
}

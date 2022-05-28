package com.example.tournamentservice.models;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name="tournaments")
public class Tournament {

    public enum Status {
        DRAFT,
        CONFIRMED,
        COMPLETE,
        DONE,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Status status;

    private Long owner;

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
    private List<Team> teams;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private List<TournamentRound> rounds;

    public Tournament() {}

    public Tournament(TournamentDefinition tournamentDefinition, List<TournamentRound> rounds, Long owner) {

        this.status = Status.DRAFT;
        this.owner = owner;

        this.name = tournamentDefinition.getName();
        this.description = tournamentDefinition.getDescription();
        this.sport = tournamentDefinition.getSport();
        this.price = tournamentDefinition.getPrice();
        this.prize = tournamentDefinition.getPrize();
        this.maxTeamsNumber = tournamentDefinition.getMaxTeamsNumber();
        this.teams = new ArrayList<>();
        this.rounds = rounds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
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

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> participants) {
        this.teams = participants;
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

    public TournamentRound getRound(int index) {
        return rounds.get(index);
    }

    public void setRounds(List<TournamentRound> rounds) {
        this.rounds = rounds;
    }

    public void addTeam(Team team) {
        if (!(teams instanceof ArrayList))
            teams = new ArrayList<>(teams);
        teams.add(team);
    }
}

package com.example.ballnet.tournament.models;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Tournament  implements Serializable {

    public enum Status {
        DRAFT,
        CONFIRMED,
        COMPLETE,
        DONE,
        CANCELLED;

        public String toPrettyString() {
            return String.format("%s%s", toString().charAt(0), toString().substring(1).toLowerCase(Locale.ROOT));
        }
    }

    private static final long serialVersionUID = 42L;

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


    private List<Team> teams;

    private List<Round> rounds;

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

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }
}

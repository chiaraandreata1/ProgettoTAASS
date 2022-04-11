package com.example.tournamentservice.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
//@Table(name="tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Long sport;
    private Double price;
    private Double prize;
    private String level;
//
////    @OneToMany
//    @ElementCollection
//    @Type(type = "com.example.tournamentservice.models.TeamType")
//    @Columns(columns = {@Column(name = "tp1"), @Column(name = "tp2")})
//    private List<Team> teams;

    @OneToMany(cascade = CascadeType.ALL)
    private List<TournamentRound> rounds;

    public Tournament() {
    }

    public Tournament(String name,
                      String description,
                      Long sport,
                      Double price,
                      Double prize,
                      String level,
                      List<TournamentRound> rounds) {
        this.name = name;
        this.description = description;
        this.sport = sport;
        this.price = price;
        this.prize = prize;
        this.level = level;
        this.rounds = rounds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<TournamentRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<TournamentRound> rounds) {
        this.rounds = rounds;
    }
}

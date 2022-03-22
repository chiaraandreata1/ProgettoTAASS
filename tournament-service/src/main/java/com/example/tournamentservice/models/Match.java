package com.example.tournamentservice.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int round;
    private int roundHeight;

//    @OneToOne
    @Columns(columns = {@Column(name = "t1p1"), @Column(name = "t1p2")})
    @Type(type = "com.example.tournamentservice.models.TeamType")
    private Team side0;
    @Columns(columns = {@Column(name = "t2p1"), @Column(name = "t2p2")})
    @Type(type = "com.example.tournamentservice.models.TeamType")
    private Team side1;
//    private Match previous0, previous1;
    private Integer points0, points1;
    private Date date;
    private String court;

    public Match() {
    }

    public Match(int round, int roundHeight) {
        this.round = round;
        this.roundHeight = roundHeight;
    }

    public Match(Team side0, int round, int roundHeight) {
        this.side0 = side0;
        this.side1 = null;
        this.points0 = 6;
        this.points1 = 0;
        this.round = round;
        this.roundHeight = roundHeight;
    }

    public Match(Team side0, Team side1, int round, int roundHeight) {
        this.side0 = side0;
        this.side1 = side1;
        this.round = round;
        this.roundHeight = roundHeight;
    }

//    public Match(Match previous0) {
//        this.previous0 = previous0;
//        this.previous1 = null;
//    }
//
//    public Match(Match previous0, Match previous1) {
//        this.previous0 = previous0;
//        this.previous1 = previous1;
//    }

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

    public int getRoundHeight() {
        return roundHeight;
    }

    public void setRoundHeight(int roundHeight) {
        this.roundHeight = roundHeight;
    }

    public Team getSide0() {
        return side0;
    }

    public void setSide0(Team side0) {
        this.side0 = side0;
    }

    public Team getSide1() {
        return side1;
    }

    public void setSide1(Team side1) {
        this.side1 = side1;
    }

//    public Match getPrevious0() {
//        return previous0;
//    }
//
//    public void setPrevious0(Match previous0) {
//        this.previous0 = previous0;
//    }
//
//    public Match getPrevious1() {
//        return previous1;
//    }
//
//    public void setPrevious1(Match previous1) {
//        this.previous1 = previous1;
//    }

    public Integer getPoints0() {
        return points0;
    }

    public void setPoints0(Integer points0) {
        this.points0 = points0;
    }

    public Integer getPoints1() {
        return points1;
    }

    public void setPoints1(Integer points1) {
        this.points1 = points1;
    }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getDate() {
        return date;
    }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setDate(Date date) {
        this.date = date;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }
}

package com.example.tournamentservice.models;

import com.example.shared.tools.DateSerialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "matches")
public class Match {

    public enum Status {
        WAITING,
        READY,
        DONE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int round;
    private int roundHeight;

    private Long reservationID;

    private Status status;

//    @OneToOne
    @Columns(columns = {@Column(name = "t1p1"), @Column(name = "t1p2")})
    @Type(type = "com.example.tournamentservice.models.TeamType")
    private Team side0;
    @Columns(columns = {@Column(name = "t2p1"), @Column(name = "t2p2")})
    @Type(type = "com.example.tournamentservice.models.TeamType")
    private Team side1;
//    private Match previous0, previous1;
    private Long points0;
    private Long points1;
    private Date date;
    private Long courtID;

    public Match() {
    }

    public Match(int round, int roundHeight) {
        this.round = round;
        this.roundHeight = roundHeight;
        this.status = Status.WAITING;
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

    public Long getPoints0() {
        return points0;
    }

    public void setPoints0(Long points0) {
        this.points0 = points0;
    }

    public Long getPoints1() {
        return points1;
    }

    public void setPoints1(Long points1) {
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

    public Long getCourtID() {
        return courtID;
    }

    public void setCourtID(Long court) {
        this.courtID = court;
    }

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }
}

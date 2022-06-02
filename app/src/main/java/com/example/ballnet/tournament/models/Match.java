package com.example.ballnet.tournament.models;

import java.io.Serializable;

public class Match implements Serializable {

    public enum Status {
        WAITING,
        READY,
        DONE
    }

    private static final long serialVersionUID = 42L;

    private Long id;

    private int round;

    private int roundHeight;

    private Long reservationID;

    private Status status;

    private Team side0, side1;

    private Long points0;

    private Long points1;

    private String date;

    private Long courtID;

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

    public Long getReservationID() {
        return reservationID;
    }

    public void setReservationID(Long reservationID) {
        this.reservationID = reservationID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getCourtID() {
        return courtID;
    }

    public void setCourtID(Long courtID) {
        this.courtID = courtID;
    }
}

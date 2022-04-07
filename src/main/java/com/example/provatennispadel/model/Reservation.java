package com.example.provatennispadel.model;

public class Reservation {
    //private String ver;
    //private String name;
    //private String api;

    private String resid;
    private String date;
    private String hour;
    private String sport;
    private String type_res; // può essere "private" oppure ...
    private String[] players;
    private String courtid;

    public String getDate() {
        return date;
    }
    public void setDate(String v ) {this.date = v;}

    public String getHour() {
        return hour;
    }
    public void setHour(String v ) {this.hour = v;}

    public String getResid() {
        return resid;
    }
    public void setResid(String v ) {this.resid = v;}

    public String getSport() {
        return sport;
    }
    public void setSport(String v ) {this.sport = v;}

    public String getType_res() {
        return type_res;
    }
    public void setType_res(String v ) {this.type_res = v;}

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    public String getCourtid() {
        return courtid;
    }

    public void setCourtid(String courtid) {
        this.courtid = courtid;
    }
}

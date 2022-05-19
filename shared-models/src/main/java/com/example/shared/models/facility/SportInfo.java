package com.example.shared.models.facility;

import java.io.Serializable;

public class SportInfo implements Serializable {

    private static final long serialVersionUID = 42L;

    private Integer playerPerTeam;

    private Integer availableCourts;

    private Integer minutesPerMatch;

    public SportInfo(Integer playerPerTeam, Integer availableCourts, Integer minutesPerMatch) {
        this.playerPerTeam = playerPerTeam;
        this.availableCourts = availableCourts;
        this.minutesPerMatch = minutesPerMatch;
    }

    public Integer getPlayerPerTeam() {
        return playerPerTeam;
    }

    public void setPlayerPerTeam(Integer playerPerTeam) {
        this.playerPerTeam = playerPerTeam;
    }

    public Integer getAvailableCourts() {
        return availableCourts;
    }

    public void setAvailableCourts(Integer availableCourts) {
        this.availableCourts = availableCourts;
    }

    public Integer getMinutesPerMatch() {
        return minutesPerMatch;
    }

    public void setMinutesPerMatch(Integer minutesPerMatch) {
        this.minutesPerMatch = minutesPerMatch;
    }
}

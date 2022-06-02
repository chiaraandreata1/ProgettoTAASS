package com.example.ballnet.facility.models;

import java.io.Serializable;
import java.util.List;

public class SportInfo implements Serializable {

    private static final long serialVersionUID = 42L;

    private String sportName;

    private Integer playerPerTeam;

    private List<Long> courtIDs;

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public Integer getPlayerPerTeam() {
        return playerPerTeam;
    }

    public void setPlayerPerTeam(Integer playerPerTeam) {
        this.playerPerTeam = playerPerTeam;
    }

    public List<Long> getCourtIDs() {
        return courtIDs;
    }

    public void setCourtIDs(List<Long> courtIDs) {
        this.courtIDs = courtIDs;
    }
}

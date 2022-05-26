package com.example.shared.models.facility;

import java.io.Serializable;
import java.util.List;

public class SportInfo implements Serializable {

    private static final long serialVersionUID = 43L;

    private Integer playerPerTeam;

    private List<Long> courtIDs;

    public SportInfo(Integer playerPerTeam, List<Long> courtIDs) {
        this.playerPerTeam = playerPerTeam;
        this.courtIDs = courtIDs;
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

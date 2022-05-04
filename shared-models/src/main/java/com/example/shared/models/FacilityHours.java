package com.example.shared.models;

import java.io.Serializable;

public class FacilityHours implements Serializable {

    private static final long serialVersionUID = 42L;

    private Integer openingTime;
    private Integer closingTime;

    public FacilityHours(Integer openingTime, Integer closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Integer getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Integer openingTime) {
        this.openingTime = openingTime;
    }

    public Integer getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Integer closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {

        int oH = openingTime / 60;
        int oM = openingTime % 60;
        int cH = closingTime / 60;
        int cM = closingTime % 60;

        return String.format("Opened from %02d:%02d to %02d:%02d", oH, oM, cH, cM);
    }
}

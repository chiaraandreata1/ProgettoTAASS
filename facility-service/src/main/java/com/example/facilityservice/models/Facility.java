package com.example.facilityservice.models;

import org.springframework.context.annotation.Bean;

import java.util.List;

public class Facility {

    private Integer openingTime = 12 * 60, closingTime = 24 * 60;

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
}

package com.example.facilityservice.models;

import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Facility {

    private Integer openingTime = 12 * 60;

    private Integer closingTime = 24 * 60;

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

    public Map<String, Integer> getHours() {
        HashMap<String, Integer> map = new HashMap<>();

        map.put("Opening", openingTime);
        map.put("Closing", closingTime);

        return map;
    }
}

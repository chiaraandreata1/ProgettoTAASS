package com.example.shared.rabbithole;

import java.io.Serializable;

public class ReservationRequest implements Serializable {

    public enum OwnerType {
        USER,
        TOURNAMENT_MATCH,
        COURSE
    }

    private static final long serialVersionUID = 42L;

    // dd/mm/yyyy
    private String date;
    private String name;
    private OwnerType ownerType;
    private Long ownerID;

    public ReservationRequest(String date, String name, OwnerType ownerType, Long ownerID) {
        this.date = date;
        this.name = name;
        this.ownerType = ownerType;
        this.ownerID = ownerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }
}

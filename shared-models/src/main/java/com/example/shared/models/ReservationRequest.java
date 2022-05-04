package com.example.shared.models;

import java.io.Serializable;

public class ReservationRequest implements Serializable {

    private static final long serialVersionUID = 42L;

    // dd/mm/yyyy
    private String date;
    private String name;
    private String ownerType;
    private String ownerID;

    public ReservationRequest(String date, String name, String ownerType, String ownerID) {
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

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }
}

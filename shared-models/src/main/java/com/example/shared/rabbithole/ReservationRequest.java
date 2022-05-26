package com.example.shared.rabbithole;

import java.io.Serializable;

public class ReservationRequest implements Serializable {

    private static final long serialVersionUID = 42L;

    // dd/mm/yyyy
    private String date;
    private ReservationOwnerType ownerType;

    private Integer hoursCount;

    private Integer courtID;
    private Long ownerID;

    // TODO courtID, remove name

    public ReservationRequest(String date, ReservationOwnerType ownerType, Integer hoursCount, Integer courtID, Long ownerID) {
        this.date = date;
        this.ownerType = ownerType;
        this.hoursCount = hoursCount;
        this.courtID = courtID;
        this.ownerID = ownerID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHoursCount() {
        return hoursCount;
    }

    public void setHoursCount(Integer hoursCount) {
        this.hoursCount = hoursCount;
    }

    public Integer getCourtID() {
        return courtID;
    }

    public void setCourtID(Integer courtID) {
        this.courtID = courtID;
    }

    public ReservationOwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(ReservationOwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
    }
}

package com.example.shared.rabbithole;

import java.io.Serializable;
import java.util.List;

public class ReservationDeleteRequest implements Serializable {

    private static final long serialVersionUID = 42L;

    private ReservationOwnerType ownerType;
    private Long ownerID;
    private List<Long> reservationIDs;

    public ReservationDeleteRequest(ReservationOwnerType ownerType, Long ownerID, List<Long> reservationIDs) {
        this.ownerType = ownerType;
        this.ownerID = ownerID;
        this.reservationIDs = reservationIDs;
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

    public List<Long> getReservationIDs() {
        return reservationIDs;
    }

    public void setReservationIDs(List<Long> reservationIDs) {
        this.reservationIDs = reservationIDs;
    }
}

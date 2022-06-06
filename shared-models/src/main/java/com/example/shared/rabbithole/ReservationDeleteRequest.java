package com.example.shared.rabbithole;

import java.io.Serializable;
import java.util.List;

public class ReservationDeleteRequest implements Serializable {

    private static final long serialVersionUID = 42L;


    private ReservationOwnerType ownerType;
    private List<DeleteCouple> toDelete;

    public ReservationDeleteRequest(ReservationOwnerType ownerType, List<DeleteCouple> toDelete) {
        this.ownerType = ownerType;
        this.toDelete = toDelete;
    }

    public ReservationOwnerType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(ReservationOwnerType ownerType) {
        this.ownerType = ownerType;
    }

    public List<DeleteCouple> getToDelete() {
        return toDelete;
    }

    public void setToDelete(List<DeleteCouple> toDelete) {
        this.toDelete = toDelete;
    }

    public static class DeleteCouple implements Serializable {
        private static final long serialVersionUID = 42L;

        private Long reservationID;
        private Long ownerID;

        public DeleteCouple(Long reservationID, Long ownerID) {
            this.reservationID = reservationID;
            this.ownerID = ownerID;
        }

        public Long getReservationID() {
            return reservationID;
        }

        public void setReservationID(Long reservationID) {
            this.reservationID = reservationID;
        }

        public Long getOwnerID() {
            return ownerID;
        }

        public void setOwnerID(Long ownerID) {
            this.ownerID = ownerID;
        }
    }
}

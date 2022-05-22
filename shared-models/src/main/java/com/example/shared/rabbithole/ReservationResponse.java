package com.example.shared.rabbithole;

import java.io.Serializable;
import java.util.*;

public class ReservationResponse implements Serializable {

    private static final long serialVersionUID = 42L;

    public static class ReservationBinding implements Serializable {

        private static final long serialVersionUID = 42L;

        private ReservationRequest request;

        private Long reservationID;

        public ReservationBinding() {
        }

        public ReservationBinding(ReservationRequest request, Long reservationID) {
            this.request = request;
            this.reservationID = reservationID;
        }
        public ReservationRequest getRequest() {
            return request;
        }

        public void setRequest(ReservationRequest request) {
            this.request = request;
        }

        public Long getReservationID() {
            return reservationID;
        }

        public void setReservationID(Long reservationID) {
            this.reservationID = reservationID;
        }
    }

    private boolean done;

    private List<ReservationBinding> bindings;

    private List<ReservationRequest> conflictingRequests;

    public ReservationResponse(List<ReservationBinding> bindings) {
        this.done = true;
        this.bindings = bindings;
    }

    public ReservationResponse(Collection<ReservationRequest> conflicts) {
        this.done = false;
        this.conflictingRequests = new ArrayList<>(conflicts);
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public List<ReservationBinding> getBindings() {
        return bindings;
    }

    public void setBindings(List<ReservationBinding> bindings) {
        this.bindings = bindings;
    }

    public List<ReservationRequest> getConflictingRequests() {
        return conflictingRequests;
    }

    public void setConflictingRequests(List<ReservationRequest> conflictingRequests) {
        this.conflictingRequests = conflictingRequests;
    }
}

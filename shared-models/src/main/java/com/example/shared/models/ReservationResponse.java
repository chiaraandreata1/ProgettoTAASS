package com.example.shared.models;

import java.io.Serializable;
import java.util.*;

public class ReservationResponse implements Serializable {

    private static final long serialVersionUID = 42L;

    private boolean done;

    private List<ReservationRequest> conflictingRequests;

    public ReservationResponse() {
        this.done = true;
    }

    public ReservationResponse(ReservationRequest... conflicts) {
        this.done = false;
        this.conflictingRequests = Arrays.asList(conflicts);
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

    public List<ReservationRequest> getConflictingRequests() {
        return conflictingRequests;
    }

    public void setConflictingRequests(List<ReservationRequest> conflictingRequests) {
        this.conflictingRequests = conflictingRequests;
    }
}

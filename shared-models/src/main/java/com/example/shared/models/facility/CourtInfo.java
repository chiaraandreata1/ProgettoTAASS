package com.example.shared.models.facility;

public class CourtInfo {

    public final Long id;

    public final SportInfo sport;

    public final boolean open;

    public CourtInfo(Long id, SportInfo sport, boolean open) {
        this.id = id;
        this.sport = sport;
        this.open = open;
    }
}

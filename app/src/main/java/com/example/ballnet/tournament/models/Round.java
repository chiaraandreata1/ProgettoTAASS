package com.example.ballnet.tournament.models;

import java.io.Serializable;
import java.util.List;

public class Round  implements Serializable {

    private static final long serialVersionUID = 42L;

    private Long id;

    private List<Match> matches;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}

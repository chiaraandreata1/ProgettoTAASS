package com.example.tournamentservice.models;

import javax.persistence.*;

@Entity
@Table(name="tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String level;
    private Integer prizemoney;
    //manca array di utenti (i partecipanti)

    public String getLevel() {
        return level;
    }
    public Integer getPrizemoney() {
        return prizemoney;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

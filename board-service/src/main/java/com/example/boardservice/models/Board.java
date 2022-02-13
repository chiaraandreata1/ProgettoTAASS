package com.example.boardservice.models;

import javax.persistence.*;

@Entity
@Table(name="boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String owner;   // atleta a cui appartiene la bacheca
    private String type;    // tipo della bacheca (lezioni, torneo...) ci sono più bacheche
    private String sport; // padel o tennis
    private String message;

    public String getowner() {
        return owner;
    }

    public String gettype() {
        return type;
    }

    public String getsport() {
        return sport;
    }

    public String getmessage() { return message; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

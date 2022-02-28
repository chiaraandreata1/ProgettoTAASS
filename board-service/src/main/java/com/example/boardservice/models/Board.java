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
    private String date; //data di creazione dell'annuncio

    public void setOwner(String owner) { this.owner = owner; }

    public void setType(String type) { this.type = type; }

    public void setSport(String sport) { this.sport = sport; }

    public void setMessage(String message) { this.message = message; }

    public void setDate(String date) { this.date = date; }

    public String getOwner() { return owner; }

    public String getType() { return type; }

    public String getSport() { return sport; }

    public String getMessage() { return message; }

    public String getDate() { return date; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

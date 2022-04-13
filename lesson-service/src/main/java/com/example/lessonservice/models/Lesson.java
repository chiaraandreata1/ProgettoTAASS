package com.example.lessonservice.models;

import javax.persistence.*;

@Entity
@Table(name="lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sporttype;
    private String instructor;
    private Integer price;
    private String player1;
    private String player2;
    private String player3;

    private Integer idReservation; //chiave esterna che porta alla reservation associata a questa lezione privata

    public Integer getIdReservation() { return idReservation; }

    public void setIdReservation(Integer idReservation) { this.idReservation = idReservation; }

    public String getPlayer1() { return player1; }

    public String getPlayer2() { return player2; }

    public String getPlayer3() { return player3; }

    public void setPlayer1(String player1) { this.player1 = player1; }

    public void setPlayer2(String player2) { this.player2 = player2; }

    public void setPlayer3(String player3) { this.player3 = player3; }

    public String getsporttype() {
        return sporttype;
    }

    public String getInstructor() {
        return instructor;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
package com.example.reservationservice.models;

import com.example.shared.tools.DateSerialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    private Date date;
    /*
    private String dateReservation;
    private Integer hourReservation;
     */
    private String sportReservation;
    private String typeReservation; //private, lesson e tournament

    @ElementCollection
    private List<Integer> players;

    public List<Integer> getPlayers() { return players; }

    public void setPlayers(List<Integer> players) { this.players = players; }

    public String getSportReservation() { return sportReservation; }

    public void setSportReservation(String sportReservation) { this.sportReservation = sportReservation; }

    /*
    public String getDateReservation() { return dateReservation; }

    public void setDateReservation(String dateReservation) {this.dateReservation = dateReservation; }

    public Integer getHourReservation() { return hourReservation; }

    public void setHourReservation(Integer hourReservation) { this.hourReservation = hourReservation; }
    */
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTypeReservation() { return typeReservation; }

    public void setTypeReservation(String typeReservation) { this.typeReservation = typeReservation; }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "court", referencedColumnName = "id")
    private Court courtReservation;

    public Court getCourtReservation() { return courtReservation; }
    public void setCourtReservation(Court court) { this.courtReservation = court; }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getDate() {
        return date;
    }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setDate(Date date) {
        this.date = date;
    }


}

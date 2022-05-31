package com.example.reservationservice.models;

import com.example.shared.rabbithole.ReservationOwnerType;
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

    @ElementCollection
    private List<Long> players;

    private Long ownerID;
    private Integer nHours;
    private Long sportReservation; //2 TENNIS SINGOLO E 3 DOUBLE TENNIS, 4 PADEL
    private ReservationOwnerType typeReservation; //user, lesson e tournament
    private Long courtReserved; //5,6,7 TENNIS  E 8,9,10 PADEL

    //GETTERS
    public Long getOwnerID() { return ownerID; }
    public Long getSportReservation() { return sportReservation; }
    public Integer getnHours() { return nHours; }
    public List<Long> getPlayers() { return players; }
    public Long getId() { return id; }
    public ReservationOwnerType getTypeReservation() { return typeReservation; }
    public Long getCourtReserved() { return courtReserved; }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getDate() { return date; }

    //SETTERS
    public void setId(Long id) { this.id = id; }
    public void setSportReservation(Long sportReservation) { this.sportReservation = sportReservation; }
    public void setOwnerID(Long ownerID) { this.ownerID = ownerID; }
    public void setnHours(Integer nHours) { this.nHours = nHours; }
    public void setPlayers(List<Long> players) { this.players = players; }
    public void setTypeReservation(ReservationOwnerType typeReservation) { this.typeReservation = typeReservation; }
    public void setCourtReserved(Long courtReserved) { this.courtReserved = courtReserved; }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setDate(Date date) { this.date = date; }








}

package com.example.reservationservice.models;

import com.example.shared.models.facility.CourtInfo;
import com.example.shared.rabbithole.ReservationOwnerType;
import com.example.shared.rabbithole.ReservationSportType;
import com.example.shared.tools.DateSerialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long ownerID;

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    private Date date;

    private Integer nHours;

    private Long sportReservation; //2 TENNIS SINGOLO E 3 DOUBLE TENNIS, 4 PADEL

    public Long getSportReservation() { return sportReservation; }

    public void setSportReservation(Long sportReservation) { this.sportReservation = sportReservation; }

    private ReservationOwnerType typeReservation; //user, lesson e tournament

    @ElementCollection
    private List<Integer> players;

    public Long getOwnerID() { return ownerID; }

    public void setOwnerID(Long ownerID) { this.ownerID = ownerID; }

    public Integer getnHours() { return nHours; }

    public void setnHours(Integer nHours) { this.nHours = nHours; }

    public List<Integer> getPlayers() { return players; }

    public void setPlayers(List<Integer> players) { this.players = players; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public ReservationOwnerType getTypeReservation() { return typeReservation; }

    public void setTypeReservation(ReservationOwnerType typeReservation) { this.typeReservation = typeReservation; }

    private Long courtReserved; //5,6,7 TENNIS  E 8,9,10 PADEL

    public Long getCourtReserved() {
        return courtReserved;
    }

    public void setCourtReserved(Long courtReserved) {
        this.courtReserved = courtReserved;
    }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getDate() {
        return date;
    }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setDate(Date date) {
        this.date = date;
    }


}

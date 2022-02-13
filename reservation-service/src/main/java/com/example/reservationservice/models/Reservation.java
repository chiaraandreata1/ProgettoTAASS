package com.example.reservationservice.models;

import javax.persistence.*;

@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sporttype;
    private String dateReservation;
    private Integer hoursReservation;

    public String getSporttype() {
        return sporttype;
    }

    public void setSporttype(String sporttype) {
        this.sporttype = sporttype;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getHoursReservation() {
        return hoursReservation;
    }

    public void setHoursReservation(Integer hoursReservation) {
        this.hoursReservation = hoursReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

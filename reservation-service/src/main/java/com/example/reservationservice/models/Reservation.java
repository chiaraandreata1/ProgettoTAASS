package com.example.reservationservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String dateReservation;
    private Integer hourReservation;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "court", referencedColumnName = "id")
    private Court courtReservation;

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Integer getHourReservation() {
        return hourReservation;
    }

    public void setHourReservation(Integer hourReservation) {
        this.hourReservation = hourReservation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Court getCourtReservation() {
        return courtReservation;
    }
    public void setCourtReservation(Court court) {
        this.courtReservation = court;
    }
}

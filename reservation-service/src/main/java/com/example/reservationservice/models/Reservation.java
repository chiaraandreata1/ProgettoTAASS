package com.example.reservationservice.models;

import javax.persistence.*;

@Entity
@Table(name="reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String dateReservation;
    private Integer hourReservation;
    private String sportReservation;
    private String player1;
    private String player2;
    private String player3;
    private String player4;

    public String getSportReservation() { return sportReservation; }

    public void setSportReservation(String sportReservation) { this.sportReservation = sportReservation; }

    public void setPlayer1(String player1) { this.player1 = player1; }

    public void setPlayer2(String player2) { this.player2 = player2; }

    public void setPlayer3(String player3) { this.player3 = player3; }

    public void setPlayer4(String player4) { this.player4 = player4; }

    public String getPlayer1() { return player1; }

    public String getPlayer2() { return player2; }

    public String getPlayer3() { return player3; }

    public String getPlayer4() { return player4; }

    public String getDateReservation() { return dateReservation; }

    public void setDateReservation(String dateReservation) {this.dateReservation = dateReservation; }

    public Integer getHourReservation() { return hourReservation; }

    public void setHourReservation(Integer hourReservation) { this.hourReservation = hourReservation; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "court", referencedColumnName = "id")
    private Court courtReservation;

    public Court getCourtReservation() { return courtReservation; }
    public void setCourtReservation(Court court) { this.courtReservation = court; }
}

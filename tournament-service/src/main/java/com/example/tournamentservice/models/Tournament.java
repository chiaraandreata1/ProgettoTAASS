package com.example.tournamentservice.models;

import javax.persistence.*;

@Entity
@Table(name="tournaments")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nameTournament;
    private String descriptionTournament;
    private Integer priceTournament;
    private String level;
    private Integer prizemoney;
    private String player1;
    private String player2;
    private String player3;
    private String player4;
    private String player5;
    private String player6;
    private String player7;
    private String player8;

    public void setNameTournament(String nameTournament) { this.nameTournament = nameTournament; }

    public void setDescriptionTournament(String descriptionTournament) { this.descriptionTournament = descriptionTournament; }

    public void setPriceTournament(Integer priceTournament) { this.priceTournament = priceTournament; }

    public void setLevel(String level) { this.level = level; }

    public void setPrizemoney(Integer prizemoney) { this.prizemoney = prizemoney; }

    public String getNameTournament() { return nameTournament; }

    public String getDescriptionTournament() { return descriptionTournament; }

    public Integer getPriceTournament() { return priceTournament; }

    public String getPlayer1() { return player1; }

    public String getPlayer2() { return player2; }

    public String getPlayer3() { return player3; }

    public String getPlayer4() { return player4; }

    public String getPlayer5() { return player5; }

    public String getPlayer6() { return player6; }

    public String getPlayer7() { return player7; }

    public String getPlayer8() { return player8; }

    public void setPlayer1(String player1) { this.player1 = player1; }

    public void setPlayer2(String player2) { this.player2 = player2; }

    public void setPlayer3(String player3) { this.player3 = player3; }

    public void setPlayer4(String player4) { this.player4 = player4; }

    public void setPlayer5(String player5) { this.player5 = player5; }

    public void setPlayer6(String player6) { this.player6 = player6; }

    public void setPlayer7(String player7) { this.player7 = player7; }

    public void setPlayer8(String player8) { this.player8 = player8; }

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

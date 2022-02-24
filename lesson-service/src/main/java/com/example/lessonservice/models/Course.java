package com.example.lessonservice.models;

import javax.persistence.*;

@Entity
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sporttype;
    private String instructor;
    private String player1;
    private String player2;
    private String player3;

    private String day1;
    private String day2;
    private String hourlesson;
    private Integer numberweeks;
    private Integer priceCourse;


    public String getDay1() { return day1; }

    public String getDay2() { return day2; }

    public String getHourlesson() { return hourlesson; }

    public void setDay1(String day1) { this.day1 = day1; }

    public void setDay2(String day2) { this.day2 = day2; }

    public void setHourlesson(String hourlesson) { this.hourlesson = hourlesson; }

    public String getPlayer1() { return player1; }

    public String getPlayer2() { return player2; }

    public String getPlayer3() { return player3; }

    public Integer getNumberweeks() { return numberweeks; }

    public void setPlayer1(String player1) { this.player1 = player1; }

    public void setPlayer2(String player2) { this.player2 = player2; }

    public void setPlayer3(String player3) { this.player3 = player3; }

    public void setNumberweeks(Integer numberweeks) { this.numberweeks = numberweeks; }

    public String getsporttype() {
        return sporttype;
    }

    public String getInstructor() {
        return instructor;
    }

    public Integer getPriceCourse() {
        return priceCourse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

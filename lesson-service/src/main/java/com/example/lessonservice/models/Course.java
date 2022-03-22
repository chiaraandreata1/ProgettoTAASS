package com.example.lessonservice.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sporttype;
    private String instructor;

    @ElementCollection
    private List<String> players;

    private String daycourse;

    private Integer hourlesson;
    private Integer numberweeks;
    private Integer priceCourse;

    private String endDateRegistration;

    public List<String> getPlayers() { return players; }

    public void setPlayers(List<String> players) { this.players = players; }

    public String getDaycourse() { return daycourse; }

    public void setDaycourse(String daycourse) { this.daycourse = daycourse; }

    public String getEndDateRegistration() { return endDateRegistration; }

    public Integer getHourlesson() { return hourlesson; }

    public void setHourlesson(Integer hourlesson) { this.hourlesson = hourlesson; }

    public Integer getNumberweeks() { return numberweeks; }

    public void setNumberweeks(Integer numberweeks) { this.numberweeks = numberweeks; }

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

    public void setSporttype(String sporttype) {
        this.sporttype = sporttype;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setPriceCourse(Integer priceCourse) {
        this.priceCourse = priceCourse;
    }

    public void setEndDateRegistration(String endDateRegistration) {
        this.endDateRegistration = endDateRegistration;
    }

    public String getSporttype() {
        return sporttype;
    }
}

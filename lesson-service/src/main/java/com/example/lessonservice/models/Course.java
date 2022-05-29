package com.example.lessonservice.models;

import com.example.shared.tools.DateSerialization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long sporttype; //2 TENNIS SINGOLO E 3 DOUBLE TENNIS, 4 PADEL
    private Long instructor;

    @ElementCollection
    private List<Long> players;

    @ElementCollection
    private List<Long> reservationsIDs;

    private Long ownerID;

    private String daycourse;
    private Integer hourlesson;
    private Integer numberweeks;
    private Integer priceCourse;
    private Long courtCourse; //5,6,7 TENNIS  E 8,9,10 PADEL

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    private Date endDateRegistration;

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    private Date firstDayLesson;

    public Long getCourtCourse() { return courtCourse; }

    public void setCourtCourse(Long courtCourse) { this.courtCourse = courtCourse; }

    public List<Long> getPlayers() { return players; }

    public void setPlayers(List<Long> players) { this.players = players; }

    public String getDaycourse() { return daycourse; }

    public void setDaycourse(String daycourse) { this.daycourse = daycourse; }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getEndDateRegistration() {
        return endDateRegistration;
    }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setEndDateRegistration(Date endDateRegistration) {
        this.endDateRegistration = endDateRegistration;
    }

    @JsonSerialize(using = DateSerialization.DateTimeSerialize.class)
    public Date getFirstDayLesson() {
        return firstDayLesson;
    }

    @JsonDeserialize(using = DateSerialization.DateTimeDeserialize.class)
    public void setFirstDayLesson(Date firstDayLesson) {
        this.firstDayLesson = firstDayLesson;
    }

    public Integer getHourlesson() { return hourlesson; }

    public void setHourlesson(Integer hourlesson) { this.hourlesson = hourlesson; }

    public Integer getNumberweeks() { return numberweeks; }

    public void setNumberweeks(Integer numberweeks) { this.numberweeks = numberweeks; }

    public Long getInstructor() {
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

    public void setSporttype(Long sporttype) {
        this.sporttype = sporttype;
    }

    public void setInstructor(Long instructor) {
        this.instructor = instructor;
    }

    public void setPriceCourse(Integer priceCourse) {
        this.priceCourse = priceCourse;
    }

    public Long getSporttype() {
        return sporttype;
    }

    public Long getOwnerID() { return ownerID; }

    public void setOwnerID(Long ownerID) { this.ownerID = ownerID; }

    public List<Long> getReservationsIDs() { return reservationsIDs; }

    public void setReservationsIDs(List<Long> reservationsIDs) { this.reservationsIDs = reservationsIDs; }
}

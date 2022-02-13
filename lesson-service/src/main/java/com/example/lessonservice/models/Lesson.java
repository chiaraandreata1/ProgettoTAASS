package com.example.lessonservice.models;

import javax.persistence.*;

@Entity
@Table(name="lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sporttype;
    private String type;        //tipo lezione: privata o parte di un corso
    private String athlete;
    private String instructor;
    private Integer price;
    private Integer hours;

    public String getsporttype() {
        return sporttype;
    }

    public String gettype() {
        return type;
    }

    public String getathlete() {
        return athlete;
    }

    public String getInstructor() {
        return instructor;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer gethours() {
        return hours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
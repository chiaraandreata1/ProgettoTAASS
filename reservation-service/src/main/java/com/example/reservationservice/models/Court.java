package com.example.reservationservice.models;

import javax.persistence.*;

@Entity
@Table(name="courts")
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String type;
    private Integer number;
    private boolean available;

    public String getType(){
        return type;
    }

    public Integer getNumber() { return number; }

    public boolean getAvailable(){
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

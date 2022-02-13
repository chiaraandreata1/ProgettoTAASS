package com.example.userservice.models;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String levelplayer;
    private String sporttype;
    private String typeuser;

    public String getUsername() {
        return username;
    }

    public String getLevelplayer() {
        return levelplayer;
    }

    public String getSporttype() {
        return sporttype;
    }

    public String getPassword() {
        return password;
    }

    public String getTypeuser() {
        return typeuser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

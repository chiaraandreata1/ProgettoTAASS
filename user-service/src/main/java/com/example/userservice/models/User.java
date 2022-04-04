package com.example.userservice.models;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String email;
    private String levelplayer;
    private String typeuser;

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getLevelplayer() {
        return levelplayer;
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

    public void setEmail(String email) { this.email = email; }

    public void setUsername(String username) { this.username = username; }

    public void setLevelplayer(String levelplayer) { this.levelplayer = levelplayer; }

    public void setTypeuser(String typeuser) { this.typeuser = typeuser; }
}

package com.example.facilityservice.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer playersPerTeam;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Sport parent;

    @ElementCollection
    private List<String> levels;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return parent != null ? String.format("%s %s", name, parent.getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getParent() {
        return parent;
    }

    public void setParent(Sport parent) {
        this.parent = parent;
    }

    public Integer getPlayersPerTeam() {
        return playersPerTeam;
    }

    public void setPlayersPerTeam(Integer playersPerTeam) {
        this.playersPerTeam = playersPerTeam;
    }

    public List<String> getLevels() {
        return parent != null ? parent.getLevels() : levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }
}

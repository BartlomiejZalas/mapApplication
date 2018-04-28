package com.zalas.mapapplication.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "continent")
public class Continent {
    private long id;
    private String name;

    public Continent() {
    }

    public Continent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

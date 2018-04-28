package com.zalas.mapapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "country")
public class Country {
    private long id;
    private String name;
    private Continent continent;

    public Country() {
    }

    public Country(long id, String name, Continent continent) {
        this.id = id;
        this.name = name;
        this.continent = continent;
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

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "continent_id")
    public Continent getContinent() {
        return continent;
    }


    @Transient
    public String getContinentName() {
        return continent.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}

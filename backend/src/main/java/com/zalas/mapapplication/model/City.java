package com.zalas.mapapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "city")
public class City {
    private long id;
    private String name;
    private Country country;

    public City() {
    }

    public City(long id, String name, Country country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @NotBlank
    public String getName() {
        return name;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "country_id")
    public Country getCountry() {
        return country;
    }

    @Transient
    public String getCountryName() {
        return country.getName();
    }

    @Transient
    public String getContinentName() {
        return country.getContinentName();
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}

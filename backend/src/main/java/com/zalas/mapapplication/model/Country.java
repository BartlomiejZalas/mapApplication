package com.zalas.mapapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "country")
public class Country {
    private long id;
    private String name;
    private Continent continent;
    private Set<City> cities;

    public Country() {
    }

    public Country(long id, String name, Continent continent, Set<City> cities) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.cities = cities;
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
    @JoinColumn(name = "continent_id")
    public Continent getContinent() {
        return continent;
    }

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<City> getCities() {
        return cities;
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

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}

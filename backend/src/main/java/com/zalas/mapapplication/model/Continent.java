package com.zalas.mapapplication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "continent")
public class Continent {
    private long id;
    private String name;
    private Set<Country> countries;

    public Continent() {
    }

    public Continent(long id, String name, Set<Country> countries) {
        this.id = id;
        this.name = name;
        this.countries = countries;
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

    @OneToMany(mappedBy = "continent", cascade = CascadeType.ALL)
    @JsonIgnore
    public Set<Country> getCountries() {
        return countries;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountries(Set<Country> countries) {
        this.countries = countries;
    }
}

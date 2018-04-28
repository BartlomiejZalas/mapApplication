package com.zalas.mapapplication.repositories;

import com.zalas.mapapplication.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountriesRepository extends JpaRepository<Country, Long> {
    List<Country> findByContinentId(long id);
}

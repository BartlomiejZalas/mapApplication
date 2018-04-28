package com.zalas.mapapplication.repositories;

import com.zalas.mapapplication.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitiesRepository extends JpaRepository<City, Long> {
    List<City> findByCountryId(long id);

    List<City> findByCountryContinentId(long id);

    List<City> findByCountryIdAndCountryContinentId(long countryId, long continentId);
}

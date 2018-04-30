package com.zalas.mapapplication.services;

import com.zalas.mapapplication.model.City;
import com.zalas.mapapplication.repositories.CitiesRepository;
import com.zalas.mapapplication.repositories.ContinentsRepository;
import com.zalas.mapapplication.repositories.CountriesRepository;
import com.zalas.mapapplication.resources.exceptionshandling.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CitiesFinder {
    private final CitiesRepository citiesRepository;
    private final CountriesRepository countriesRepository;
    private final ContinentsRepository continentsRepository;

    @Autowired
    public CitiesFinder(CitiesRepository citiesRepository, CountriesRepository countriesRepository, ContinentsRepository continentsRepository) {
        this.citiesRepository = citiesRepository;
        this.countriesRepository = countriesRepository;
        this.continentsRepository = continentsRepository;
    }

    public List<City> findCities(Optional<Long> continentId, Optional<Long> countryId) throws ElementNotFoundException {
        if (continentId.isPresent() && countryId.isPresent()) {
            validateContinent(continentId.get());
            validateCountry(countryId.get());
            validateCountryContinentConsistency(countryId.get(), continentId.get());
            return citiesRepository.findByCountryIdAndCountryContinentId(countryId.get(), continentId.get());
        } else if (continentId.isPresent()) {
            validateContinent(continentId.get());
            return citiesRepository.findByCountryContinentId(continentId.get());
        } else if (countryId.isPresent()) {
            validateCountry(countryId.get());
            return citiesRepository.findByCountryId(countryId.get());
        }
        return citiesRepository.findAll();
    }

    private void validateCountryContinentConsistency(long countryId, long continentId) throws ElementNotFoundException {
        if (countriesRepository.findById(countryId).get().getContinent().getId() != continentId) {
            throw new ElementNotFoundException("Country with id=" + countryId + " is not on continent with id=" + continentId + "!");
        }
    }

    private void validateCountry(long countryId) throws ElementNotFoundException {
        if (!countriesRepository.findById(countryId).isPresent()) {
            throw new ElementNotFoundException("Country with id=" + countryId + " not found!");
        }
    }

    private void validateContinent(long continentId) throws ElementNotFoundException {
        if (!continentsRepository.findById(continentId).isPresent()) {
            throw new ElementNotFoundException("Continent with id=" + continentId + " not found!");
        }
    }
}

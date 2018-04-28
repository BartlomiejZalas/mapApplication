package com.zalas.mapapplication.resources;

import com.zalas.mapapplication.model.City;
import com.zalas.mapapplication.model.Country;
import com.zalas.mapapplication.repositories.CitiesRepository;
import com.zalas.mapapplication.repositories.ContinentsRepository;
import com.zalas.mapapplication.repositories.CountriesRepository;
import com.zalas.mapapplication.resources.exceptionshandling.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(CitiesController.CITIES_PATH)
public class CitiesController {
    static final String CITIES_PATH = "cities";

    private ContinentsRepository continentsRepository;
    private CountriesRepository countriesRepository;
    private CitiesRepository citiesRepository;

    @Autowired
    public CitiesController(ContinentsRepository continentsRepository, CountriesRepository countriesRepository, CitiesRepository citiesRepository) {
        this.continentsRepository = continentsRepository;
        this.countriesRepository = countriesRepository;
        this.citiesRepository = citiesRepository;
    }

    @GetMapping("/{cityId}")
    public City get(@PathVariable("cityId") long cityId) throws ElementNotFoundException {
        Optional<City> city = citiesRepository.findById(cityId);
        if (city.isPresent()) {
            return city.get();
        }
        throw new ElementNotFoundException("City with id=" + cityId + " not found!");
    }

    @GetMapping
    public List<City> getAll(
            @RequestParam(required = false) Optional<Long> continentId,
            @RequestParam(required = false) Optional<Long> countryId
    ) throws ElementNotFoundException {
        if (continentId.isPresent() && countryId.isPresent()) {
            validateContinent(continentId.get());
            validateCountry(countryId.get());
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

    @DeleteMapping("/{cityId}")
    public void delete(@PathVariable("cityId") long cityId) throws ElementNotFoundException {
        Optional<City> city = citiesRepository.findById(cityId);
        if (city.isPresent()) {
            citiesRepository.delete(city.get());
        } else {
            throw new ElementNotFoundException("City with id=" + cityId + " not found!");
        }
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<City> save(
            @RequestBody City city, @RequestParam long countryId, UriComponentsBuilder ucb
    ) throws ElementNotFoundException {
        validateCountry(countryId);

        Country parentCountry = countriesRepository.findById(countryId).get();
        City savedCity = citiesRepository.save(new City(0, city.getName(), parentCountry));
        return prepareResponse(ucb, savedCity);
    }

    private void validateCountry(@RequestParam(required = false) long countryId) throws ElementNotFoundException {
        if (!countriesRepository.findById(countryId).isPresent()) {
            throw new ElementNotFoundException("Country with id=" + countryId + " not found!");
        }
    }

    private void validateContinent(@RequestParam(required = false) long continentId) throws ElementNotFoundException {
        if (!continentsRepository.findById(continentId).isPresent()) {
            throw new ElementNotFoundException("Continent with id=" + continentId + " not found!");
        }
    }

    private ResponseEntity<City> prepareResponse(UriComponentsBuilder ucb, City savedCity) {
        URI locationUri = ucb.path(CITIES_PATH + "/").path(String.valueOf(savedCity.getId())).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(savedCity, headers, HttpStatus.CREATED);
    }

}

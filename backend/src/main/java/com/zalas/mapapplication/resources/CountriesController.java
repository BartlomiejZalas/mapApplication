package com.zalas.mapapplication.resources;

import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.model.Country;
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
@RequestMapping(CountriesController.COUNTRIES_PATH)
@CrossOrigin(origins = "http://localhost:4200")
public class CountriesController {
    static final String COUNTRIES_PATH = "countries";

    private CountriesRepository countriesRepository;
    private ContinentsRepository continentsRepository;

    @Autowired
    public CountriesController(CountriesRepository countriesRepository, ContinentsRepository continentsRepository) {
        this.countriesRepository = countriesRepository;
        this.continentsRepository = continentsRepository;
    }

    @GetMapping("/{countryId}")
    public Country get(@PathVariable("countryId") long countryId) throws ElementNotFoundException {
        Optional<Country> country = countriesRepository.findById(countryId);
        if (country.isPresent()) {
            return country.get();
        }
        throw new ElementNotFoundException("Country with id=" + countryId + " not found!");
    }

    @GetMapping
    public List<Country> getAll(@RequestParam(required = false) Optional<Long> continentId) throws ElementNotFoundException {
        if (continentId.isPresent()) {
            validateContinent(continentId);
            return countriesRepository.findByContinentId(continentId.get());
        }
        return countriesRepository.findAll();
    }

    @DeleteMapping("/{countryId}")
    public void delete(@PathVariable("countryId") long countryId) throws ElementNotFoundException {
        Optional<Country> country = countriesRepository.findById(countryId);
        if (country.isPresent()) {
            countriesRepository.delete(country.get());
        } else {
            throw new ElementNotFoundException("Country with id=" + countryId + " not found!");
        }
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Country> save(@RequestBody Country country, @RequestParam long continentId, UriComponentsBuilder ucb) throws ElementNotFoundException {
        Optional<Continent> continent = continentsRepository.findById(continentId);
        if (continent.isPresent()) {
            Country savedCountry = countriesRepository.save(new Country(0, country.getName(), continent.get(), null));
            return prepareResponse(ucb, savedCountry);
        }
        throw new ElementNotFoundException("Continent with id=" + continentId + " not found!");

    }

    private void validateContinent(@RequestParam(required = false) Optional<Long> continentId) throws ElementNotFoundException {
        if (!continentsRepository.findById(continentId.get()).isPresent()) {
            throw new ElementNotFoundException("Continent with id=" + continentId.get() + " not found!");
        }
    }

    private ResponseEntity<Country> prepareResponse(UriComponentsBuilder ucb, Country savedCountry) {
        URI locationUri = ucb.path(COUNTRIES_PATH + "/").path(String.valueOf(savedCountry.getId())).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);
        return new ResponseEntity<>(savedCountry, headers, HttpStatus.CREATED);
    }

}

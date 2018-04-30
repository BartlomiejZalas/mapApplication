package com.zalas.mapapplication.resources;

import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.repositories.ContinentsRepository;
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
@RequestMapping(ContinentsController.CONTINENTS_PATH)
@CrossOrigin(origins = "http://localhost:4200")
public class ContinentsController {

    static final String CONTINENTS_PATH = "/continents";
    private ContinentsRepository continentsRepository;

    @Autowired
    public ContinentsController(ContinentsRepository continentsRepository) {
        this.continentsRepository = continentsRepository;
    }

    @GetMapping("/{continentId}")
    public Continent get(@PathVariable("continentId") long continentId) throws ElementNotFoundException {
        Optional<Continent> continent = continentsRepository.findById(continentId);
        if (continent.isPresent()) {
            return continent.get();
        }
        throw new ElementNotFoundException("Continent with id=" + continentId + " not found!");
    }

    @GetMapping
    public List<Continent> getAll() {
        return continentsRepository.findAll();
    }

    @DeleteMapping("/{continentId}")
    public void delete(@PathVariable("continentId") long continentId) throws ElementNotFoundException {
        Optional<Continent> continent = continentsRepository.findById(continentId);
        if (continent.isPresent()) {
            continentsRepository.delete(continent.get());
        } else {
            throw new ElementNotFoundException("Continent with id=" + continentId + " not found!");
        }
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Continent> save(@RequestBody Continent continent, UriComponentsBuilder uriComponentsBuilder) {
        Continent savedContinent = continentsRepository.save(continent);
        return createResponse(uriComponentsBuilder, savedContinent);
    }

    private ResponseEntity<Continent> createResponse(UriComponentsBuilder ucb, Continent savedContinent) {
        URI locationUri = ucb.path(CONTINENTS_PATH + "/").path(String.valueOf(savedContinent.getId())).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(locationUri);

        return new ResponseEntity<>(savedContinent, headers, HttpStatus.CREATED);
    }


}

package com.zalas.mapapplication.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalas.mapapplication.model.City;
import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.model.Country;
import com.zalas.mapapplication.repositories.CitiesRepository;
import com.zalas.mapapplication.repositories.ContinentsRepository;
import com.zalas.mapapplication.repositories.CountriesRepository;
import com.zalas.mapapplication.resources.exceptionshandling.RestError;
import com.zalas.mapapplication.resources.exceptionshandling.RestExceptionHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CitiesControllerTest {

    private static final City CITY = new City(1, "Walbrzych", new Country(1, "Poland", new Continent(), null));
    private static final City ANOTHER_CITY = new City(1, "Wroclaw", new Country(1, "Poland", new Continent(), null));

    private MockMvc mockMvc;

    @Mock
    private ContinentsRepository continentsRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private CitiesRepository citiesRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CitiesController(continentsRepository, countriesRepository, citiesRepository))
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
    }

    @Test
    public void get_shouldReturnCityForGivenId_whenCityWithProvidedIdExists() throws Exception {
        given(citiesRepository.findById(1L)).willReturn(Optional.of(CITY));

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(CITY)));
    }

    @Test
    public void get_shouldReturnError_whenCountryWithProvidedIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("City with id=1 not found!"))));
    }

    @Test
    public void getAll_shouldReturnAllCities_whenNoContinentAndCountryGivenInParams() throws Exception {
        ArrayList<City> cities = newArrayList(CITY, ANOTHER_CITY);
//        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));
//        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));
        given(citiesRepository.findAll()).willReturn(cities);

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(cities)));
    }

    @Test
    public void getAll_shouldReturnCitiesFromGiveCountry_whenCountryGivenInParam() throws Exception {
        ArrayList<City> cities = newArrayList(CITY);
        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));
        given(citiesRepository.findByCountryId(1L)).willReturn(cities);

        mockMvc.perform(get("/cities?countryId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(cities)));
    }

    @Test
    public void getAll_shouldReturnCitiesFromGiveContinent_whenContinentGivenInParam() throws Exception {
        ArrayList<City> cities = newArrayList(CITY);
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));
        given(citiesRepository.findByCountryContinentId(1L)).willReturn(cities);

        mockMvc.perform(get("/cities?continentId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(cities)));
    }

    @Test
    public void getAll_shouldReturnCitiesFromGiveContinentAndCountry_whenContinentAndCountryGivenInParams() throws Exception {
        ArrayList<City> cities = newArrayList(CITY, ANOTHER_CITY);
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));
        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));
        given(citiesRepository.findByCountryIdAndCountryContinentId(1L, 1L)).willReturn(cities);

        mockMvc.perform(get("/cities?continentId=1&countryId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(cities)));
    }

    @Test
    public void getAll_shouldReturnError_whenIncorrectContinentAndCorrectCountryGiven() throws Exception {
        mockMvc.perform(get("/cities?continentId=42&countryId=1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=42 not found!"))));
    }

    @Test
    public void getAll_shouldReturnError_whenIncorrectCountryAndCorrectContinentGiven() throws Exception {
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));

        mockMvc.perform(get("/cities?continentId=1&countryId=42"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Country with id=42 not found!"))));
    }

    @Test
    public void getAll_shouldReturnError_whenIncorrectContinentGiven() throws Exception {
        mockMvc.perform(get("/cities?continentId=42"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=42 not found!"))));
    }

    @Test
    public void getAll_shouldReturnError_whenIncorrectCountryGiven() throws Exception {
        mockMvc.perform(get("/cities?countryId=42"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Country with id=42 not found!"))));
    }

    @Test
    public void delete_shouldDeleteCity_whenCorrectIdGiven() throws Exception {
        given(citiesRepository.findById(1L)).willReturn(Optional.of(CITY));

        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void delete_shouldReturnError_whenIncorrectIdGiven() throws Exception {
        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("City with id=1 not found!"))));
    }

    @Test
    public void save_shouldSaveCity_whenCorrectCountryGiven() throws Exception {
        given(citiesRepository.save(argThat(new CityMatcher(CITY.getName())))).willReturn(CITY);
        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));

        mockMvc.perform(post("/cities?countryId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJson(CITY)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(CITY)))
                .andExpect(header().string("Location", "http://localhost/cities/1"));
    }

    @Test
    public void save_shouldReturnError_whenIncorrectCountryGiven() throws Exception {
        mockMvc.perform(post("/cities?countryId=42")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJson(CITY)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Country with id=42 not found!"))));
    }

    private String asJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(o);
    }

    class CityMatcher implements ArgumentMatcher<City> {
        private String expectedName;

        public CityMatcher(String expectedName) {
            this.expectedName = expectedName;
        }

        @Override
        public boolean matches(City city) {
            return expectedName.equals(city.getName());
        }
    }
}
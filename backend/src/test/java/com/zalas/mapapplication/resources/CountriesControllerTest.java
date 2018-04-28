package com.zalas.mapapplication.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.model.Country;
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
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CountriesControllerTest {

    private static final Country COUNTRY = new Country(1, "Poland", new Continent(), null);
    private static final Country ANOTHER_COUNTRY = new Country(1, "Germany", new Continent(), null);

    private MockMvc mockMvc;

    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private ContinentsRepository continentsRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new CountriesController(countriesRepository, continentsRepository))
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
    }

    @Test
    public void get_shouldReturnCountryForGivenId_whenCountryWithProvidedIdExists() throws Exception {
        given(countriesRepository.findById(1L)).willReturn(Optional.of(COUNTRY));

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(COUNTRY)));
    }

    @Test
    public void get_shouldReturnError_whenCountryWithProvidedIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Country with id=1 not found!"))));
    }

    @Test
    public void getAll_shouldReturnAllCountries_whenNotContinentIdGiven() throws Exception {
        ArrayList<Country> countries = newArrayList(COUNTRY, ANOTHER_COUNTRY);
        given(countriesRepository.findAll()).willReturn(countries);

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(countries)));
    }

    @Test
    public void getAll_shouldReturnCountriesFromGivenContinent_whenContinentIdGiven() throws Exception {
        ArrayList<Country> countries = newArrayList(COUNTRY);
        given(countriesRepository.findByContinentId(1)).willReturn(countries);
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));

        mockMvc.perform(get("/countries?continentId=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(countries)));
    }

    @Test
    public void getAll_shouldReturnError_whenIncorrectContinentIdGiven() throws Exception {
        mockMvc.perform(get("/countries?continentId=1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=1 not found!"))));
    }

    @Test
    public void delete_shouldDeleteCountry_whenCorrectIdGiven() throws Exception {
        given(countriesRepository.findById(1L)).willReturn(Optional.of(COUNTRY));

        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isOk());
        then(countriesRepository).should().delete(COUNTRY);
    }

    @Test
    public void delete_shouldReturnError_whenIncorrectIdGiven() throws Exception {
        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Country with id=1 not found!"))));
    }

    @Test
    public void save_shouldSaveCountry_whenCorrectContinentGiven() throws Exception {
        given(countriesRepository.save(argThat(new CountryMatcher("Poland")))).willReturn(COUNTRY);
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));

        mockMvc.perform(post("/countries?continentId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJson(COUNTRY)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(COUNTRY)))
                .andExpect(header().string("Location", "http://localhost/countries/1"));
    }

    @Test
    public void save_shouldReturnError_whenIncorrectContinentGiven() throws Exception {
        mockMvc.perform(post("/countries?continentId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJson(COUNTRY)))

                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=1 not found!"))));
    }

    private String asJson(Object continent) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(continent);
    }

    class CountryMatcher implements ArgumentMatcher<Country> {
        private String expectedName;

        CountryMatcher(String expectedName) {
            this.expectedName = expectedName;
        }

        @Override
        public boolean matches(Country country) {
            return expectedName.equals(country.getName());
        }
    }
}
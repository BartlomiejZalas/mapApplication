package com.zalas.mapapplication.services;

import com.zalas.mapapplication.model.City;
import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.model.Country;
import com.zalas.mapapplication.repositories.CitiesRepository;
import com.zalas.mapapplication.repositories.ContinentsRepository;
import com.zalas.mapapplication.repositories.CountriesRepository;
import com.zalas.mapapplication.resources.exceptionshandling.ElementNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class CitiesFinderTest {

    private static final City CITY = new City(1, "Walbrzych", new Country(1, "Poland", new Continent(), null));
    private static final City ANOTHER_CITY = new City(1, "Wroclaw", new Country(1, "Poland", new Continent(), null));
    private static final List<City> CITIES = newArrayList(CITY, ANOTHER_CITY);

    @Mock
    private CitiesRepository citiesRepository;
    @Mock
    private CountriesRepository countriesRepository;
    @Mock
    private ContinentsRepository continentsRepository;

    private CitiesFinder citiesFinder;

    @Before
    public void setUp() throws Exception {
        citiesFinder = new CitiesFinder(citiesRepository, countriesRepository, continentsRepository);
    }

    @Test
    public void findCities_shouldReturnAllCities_whenNoContinentAndCountryGivenInParams() throws Exception {
        given(citiesRepository.findAll()).willReturn(CITIES);
        Optional<Long> continentId = Optional.empty();
        Optional<Long> countryId = Optional.empty();

        List<City> result = citiesFinder.findCities(continentId, countryId);
        assertThat(result, is(CITIES));
    }

    @Test
    public void findCities_shouldReturnCitiesFromGiveCountry_whenCountryGivenInParam() throws Exception {
        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country()));
        given(citiesRepository.findByCountryId(1L)).willReturn(CITIES);
        Optional<Long> continentId = Optional.empty();
        Optional<Long> countryId = Optional.of(1L);

        List<City> result = citiesFinder.findCities(continentId, countryId);

        assertThat(result, is(CITIES));
    }

    @Test
    public void findCities_shouldReturnCitiesFromGiveContinent_whenContinentGivenInParam() throws Exception {
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));
        given(citiesRepository.findByCountryContinentId(1L)).willReturn(CITIES);
        Optional<Long> continentId = Optional.of(1L);
        Optional<Long> countryId = Optional.empty();

        List<City> result = citiesFinder.findCities(continentId, countryId);

        assertThat(result, is(CITIES));
    }

    @Test
    public void findCities_shouldReturnCitiesFromGiveContinentAndCountry_whenContinentAndCountryGivenInParams() throws Exception {
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));
        given(countriesRepository.findById(1L)).willReturn(Optional.of(new Country(1L, "poland", new Continent(1L, "", null), null)));
        given(citiesRepository.findByCountryIdAndCountryContinentId(1L, 1L)).willReturn(CITIES);
        Optional<Long> continentId = Optional.of(1L);
        Optional<Long> countryId = Optional.of(1L);

        List<City> result = citiesFinder.findCities(continentId, countryId);

        assertThat(result, is(CITIES));
    }

    @Test(expected = ElementNotFoundException.class)
    public void findCities_shouldReturnError_whenIncorrectContinentAndCorrectCountryGiven() throws Exception {
        Optional<Long> continentId = Optional.of(1L);
        Optional<Long> countryId = Optional.of(1L);

        citiesFinder.findCities(continentId, countryId);
    }

    @Test(expected = ElementNotFoundException.class)
    public void findCities_shouldReturnError_whenIncorrectCountryAndCorrectContinentGiven() throws Exception {
        given(continentsRepository.findById(1L)).willReturn(Optional.of(new Continent()));
        Optional<Long> continentId = Optional.of(1L);
        Optional<Long> countryId = Optional.of(1L);

        citiesFinder.findCities(continentId, countryId);
    }

    @Test(expected = ElementNotFoundException.class)
    public void findCities_shouldReturnError_whenIncorrectContinentGiven() throws Exception {
        Optional<Long> continentId = Optional.of(1L);
        Optional<Long> countryId = Optional.empty();

        citiesFinder.findCities(continentId, countryId);
    }

    @Test(expected = ElementNotFoundException.class)
    public void findCities_shouldReturnError_whenIncorrectCountryGiven() throws Exception {
        Optional<Long> continentId = Optional.empty();
        Optional<Long> countryId = Optional.of(1L);

        citiesFinder.findCities(continentId, countryId);
    }

    @Test(expected = ElementNotFoundException.class)
    public void findCities_shouldReturnError_whenContinentAndCountryIdsNotMatch() throws Exception {
        Continent europe = new Continent(1L, "Europe", null);
        Continent asia = new Continent(3L, "Asia", null);
        given(continentsRepository.findById(3L)).willReturn(Optional.of(asia));
        given(countriesRepository.findById(2L)).willReturn(Optional.of(new Country(2L, "Poland", europe, null)));
        Optional<Long> continentId = Optional.of(3L);
        Optional<Long> countryId = Optional.of(2L);

        citiesFinder.findCities(continentId, countryId);
    }
}
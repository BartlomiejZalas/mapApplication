package com.zalas.mapapplication.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zalas.mapapplication.model.Continent;
import com.zalas.mapapplication.repositories.ContinentsRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ContinentsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ContinentsRepository continentsRepository;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new ContinentsController(continentsRepository))
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
    }

    @Test
    public void get_shouldReturnJsonRepresentationOfContinent_whenContinentExists() throws Exception {
        Continent continent = new Continent(1, "Europe", null);
        given(continentsRepository.findById(1l)).willReturn(Optional.of(continent));

        mockMvc.perform(get("/continents/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(continent)));
    }

    @Test
    public void get_shouldReturnNotFoundError_whenContinentDoesNotExist() throws Exception {
        mockMvc.perform(get("/continents/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=1 not found!"))));
    }

    @Test
    public void findAll_shouldReturnAllContinentsFromRepository_whenThereAreContinents() throws Exception {
        ArrayList<Continent> continents = newArrayList(new Continent(1, "Europe", null), new Continent(2, "North America", null));
        given(continentsRepository.findAll()).willReturn(continents);

        mockMvc.perform(get("/continents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(continents)));
    }

    @Test
    public void delete_shouldReturnSuccessStatus_whenContinentWasDeleted() throws Exception {
        Continent continent = new Continent(1, "Europe", null);
        given(continentsRepository.findById(1l)).willReturn(Optional.of(continent));

        mockMvc.perform(delete("/continents/1"))
                .andExpect(status().isOk());
        then(continentsRepository).should().delete(continent);
    }

    @Test
    public void delete_shouldReturnNotFoundError_whenContinentDoesNotExist() throws Exception {
        mockMvc.perform(get("/continents/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(new RestError("Continent with id=1 not found!"))));
    }

    @Test
    public void save_shouldAddContinentToRepository() throws Exception {
        Continent requestContinent = new Continent(0, "Europe", null);
        Continent createdContinent = new Continent(1, "Europe", null);
        given(continentsRepository.save(argThat(new ContinentMatcher("Europe")))).willReturn(createdContinent);

        mockMvc.perform(post("/continents")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJson(requestContinent)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(asJson(createdContinent)))
                .andExpect(header().string("Location", "http://localhost/continents/1"));
    }

    private String asJson(Object continent) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(continent);
    }

    class ContinentMatcher implements ArgumentMatcher<Continent> {
        private String expectedName;

        public ContinentMatcher(String expectedName) {
            this.expectedName = expectedName;
        }

        @Override
        public boolean matches(Continent o) {
            return expectedName.equals(o.getName());
        }
    }
}
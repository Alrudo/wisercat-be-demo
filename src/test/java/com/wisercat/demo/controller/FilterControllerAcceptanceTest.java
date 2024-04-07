package com.wisercat.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisercat.demo.dto.CriterionDTO;
import com.wisercat.demo.dto.FilterDTO;
import com.wisercat.demo.entity.CriterionEntity;
import com.wisercat.demo.entity.FilterEntity;
import com.wisercat.demo.util.CriterionParam;
import com.wisercat.demo.util.CriterionType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilterControllerAcceptanceTest extends CustomPostgreSQLContainer {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String API_URL = "/api/v1/demo";

    @Test
    @Order(1)
    void whenGetAllFilters_ShouldReturnListOfFilters() throws Exception {
        mockMvc.perform(get(API_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void whenValidCreateFilterRequest_thenShouldSaveFilter() throws Exception {
        var filterDTO = new FilterDTO("My filter", 1,
                List.of(new CriterionDTO("title", "startsWith", "Meow")));
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("My filter"))
                .andExpect(jsonPath("$.criteria").isArray())
                .andExpect(jsonPath("$.criteria", hasSize(1)))
                .andExpect(jsonPath("$.criteria[0].type").value("title"))
                .andExpect(jsonPath("$.criteria[0].param").value("startsWith"))
                .andExpect(jsonPath("$.criteria[0].value").value("Meow"));
    }

    @ParameterizedTest
    @CsvSource({
            "Valid name, 3, title, startsWith",
            "'', 1, title, startsWith",
            "Valid name, 1, title, STARTSWITH",
            "Valid name, 1, TITLE, startsWith"})
    void whenInvalidCreateFilterRequest_thenShouldReturn400(String name,
                                                            int selection,
                                                            String type,
                                                            String param) throws Exception {
        var filterDTO = new FilterDTO(name, selection,
                List.of(new CriterionDTO(type, param, "Meow")));

        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filterDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void whenFilterIdNotInDatabase_updateFilterShouldReturn404() throws Exception {
        var criterion = CriterionEntity.builder()
                .id(1L)
                .type(CriterionType.AMOUNT.toString().toLowerCase())
                .param(CriterionParam.EQUAL.getValue())
                .value("2")
                .build();

        var filter = FilterEntity.builder()
                .id(100L)
                .name("Filter name")
                .criteria(List.of(criterion))
                .selection(1)
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.getReasonPhrase()));
    }

    @ParameterizedTest
    @CsvSource({
            "Valid name, 3, amount, >, 123",
            "'', 1, amount, >, 123",
            "Valid name, 1, amount, startsWith, 123",
            "Valid name, 1, AMOUNT, >, 123",
            "Valid name, 1, amount, >, vw4qwv4"})
    void whenFilterDataNotValid_updateFilterShouldReturn400(String name, int selection,
                                                            String type, String param, String value) throws Exception {
        var criterion = CriterionEntity.builder()
                .id(1L)
                .type(type)
                .param(param)
                .value(value)
                .build();

        var filter = FilterEntity.builder()
                .id(1L)
                .name(name)
                .criteria(List.of(criterion))
                .selection(selection)
                .build();

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.getReasonPhrase()));
    }

    @Test
    void whenFilterDataValid_updateFilterShouldUpdateFilter() throws Exception {
        var name = "New Cool Filter Name";
        var selection = 2;
        var val1 = "212";
        var val2 = "Meow";
        var type1 = CriterionType.AMOUNT.toString().toLowerCase();
        var type2 = CriterionType.TITLE.toString().toLowerCase();
        var param1 = CriterionParam.EQUAL.getValue();
        var param2 = CriterionParam.STARTS_WITH.getValue();

        var criterionOne = CriterionEntity.builder()
                .id(1L)
                .type(type1)
                .param(param1)
                .value(val1)
                .build();
        var criterionTwo = CriterionEntity.builder()
                .id(2L)
                .type(type2)
                .param(param2)
                .value(val2)
                .build();

        var filter = FilterEntity.builder()
                .id(1L)
                .name(name)
                .criteria(List.of(criterionOne, criterionTwo))
                .selection(selection)
                .build();

        criterionOne.setFilter(filter);
        criterionTwo.setFilter(filter);

        mockMvc.perform(put(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.selection").value(selection))
                .andExpect(jsonPath("$.criteria").isArray())
                .andExpect(jsonPath("$.criteria", hasSize(2)))
                .andExpect(jsonPath("$.criteria[0].type").value(type1))
                .andExpect(jsonPath("$.criteria[0].param").value(param1))
                .andExpect(jsonPath("$.criteria[0].value").value(val1))
                .andExpect(jsonPath("$.criteria[1].type").value(type2))
                .andExpect(jsonPath("$.criteria[1].param").value(param2))
                .andExpect(jsonPath("$.criteria[1].value").value(val2));
    }

    @Test
    void whenFilterIdNotInDatabase_deleteFilterShouldReturn204() throws Exception {
        mockMvc.perform(delete(API_URL + "/100"))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenFilterIdInDatabase_deleteFilterShouldReturn204() throws Exception {
        mockMvc.perform(delete(API_URL + "/2"))
                .andExpect(status().isNoContent());
    }
}
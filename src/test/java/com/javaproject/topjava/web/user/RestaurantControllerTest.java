package com.javaproject.topjava.web.user;


import com.javaproject.topjava.mapper.RestaurantMapper;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.to.RestaurantTo;
import com.javaproject.topjava.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javaproject.topjava.web.RestaurantTestData.*;

import static com.javaproject.topjava.web.RestaurantTestData.RESTAURANT_1;
import static com.javaproject.topjava.web.admin.UserTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    RestaurantRepository repository;

    @Autowired
    RestaurantMapper mapper;



    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-name?name=" + RESTAURANT_1.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(mapper.toDto(RESTAURANT_1)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(mapper.toDto(RESTAURANT_1)));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        List<RestaurantTo> restaurants = Stream.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4)
                .sorted(Comparator.comparing(Restaurant::getName))
                .map(restaurant -> mapper.toDto(restaurant)).collect(Collectors.toList());

        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurants));
    }

}
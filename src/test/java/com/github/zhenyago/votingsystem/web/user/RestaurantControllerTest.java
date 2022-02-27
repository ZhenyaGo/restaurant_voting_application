package com.github.zhenyago.votingsystem.web.user;


import com.github.zhenyago.votingsystem.mapper.RestaurantMapper;
import com.github.zhenyago.votingsystem.model.Restaurant;
import com.github.zhenyago.votingsystem.repository.RestaurantRepository;
import com.github.zhenyago.votingsystem.to.RestaurantTo;
import com.github.zhenyago.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;

import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.RESTAURANT_1;
import static com.github.zhenyago.votingsystem.web.data.UserTestData.*;
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
    void getByName() throws Exception {
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

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWithMenu() throws Exception {
        List<Restaurant> restaurants = getRestaurantWithMenuForToday();
        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RES_MATCHER.contentJson(restaurants));
    }
}
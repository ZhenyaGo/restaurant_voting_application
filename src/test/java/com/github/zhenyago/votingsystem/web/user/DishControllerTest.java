package com.github.zhenyago.votingsystem.web.user;

import com.github.zhenyago.votingsystem.mapper.DishMapper;
import com.github.zhenyago.votingsystem.repository.DishRepository;
import com.github.zhenyago.votingsystem.to.DishTo;
import com.github.zhenyago.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;
import static com.github.zhenyago.votingsystem.web.data.DishTestData.*;
import static com.github.zhenyago.votingsystem.web.data.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = DishController.REST_URL + '/';

    @Autowired
    DishRepository dishRepository;

    @Autowired
    DishMapper mapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantDishesByDate() throws Exception {

        List<DishTo> dishes = Stream.of(DISH_1, DISH_2, DISH_3)
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        perform(MockMvcRequestBuilders.
                 get(REST_URL + "restaurants/" + RESTAURANT_1_ID + "/by-date?date=" + LocalDate.of(2021, 12, 1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishes));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantDishesForToday() throws Exception {
        List<DishTo> newDishesForToday = newDishes().stream()
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        perform(MockMvcRequestBuilders.
                get(REST_URL + "restaurants/" + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(newDishesForToday));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurantDishes() throws Exception {

        List<DishTo> dishes = Stream.of(DISH_1, DISH_2, DISH_3, DISH_13, DISH_14, DISH_15)
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        perform(MockMvcRequestBuilders.
                get(REST_URL + "/restaurants/" + RESTAURANT_1_ID + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishes));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1.id()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(mapper.toDto(DISH_1)));
    }
}
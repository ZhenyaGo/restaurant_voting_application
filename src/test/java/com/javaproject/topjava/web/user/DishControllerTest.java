package com.javaproject.topjava.web.user;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.javaproject.topjava.web.data.RestaurantTestData.*;
import static com.javaproject.topjava.web.data.DishTestData.*;
import static com.javaproject.topjava.web.data.UserTestData.USER_MAIL;
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
                 get(REST_URL + "restaurant/" + RESTAURANT_1_ID + "/by-date?Date=" + LocalDate.of(2021, 12, 1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(dishes));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantDishesForToday() throws Exception {
        List<Dish> dishes = dishRepository.saveAll(getNewDishes());
        List<DishTo> newDishesForToday = getNewDishesWithId().stream()
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        perform(MockMvcRequestBuilders.
                get(REST_URL + "restaurant/" + RESTAURANT_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(newDishesForToday));
    }


    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllRestaurantDishes() throws Exception {

        List<DishTo> dishes = Stream.of(DISH_1, DISH_2, DISH_3)
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        perform(MockMvcRequestBuilders.
                get(REST_URL + "all/restaurant/" + RESTAURANT_1_ID))
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
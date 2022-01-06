package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.util.JsonUtil;
import com.javaproject.topjava.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.stream.Collectors;

import static com.javaproject.topjava.web.data.DishTestData.*;
import static com.javaproject.topjava.web.data.DishTestData.getUpdated;
import static com.javaproject.topjava.web.data.RestaurantTestData.*;
import static com.javaproject.topjava.web.data.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminDishController.REST_URL + '/';

    @Autowired
    DishRepository dishRepository;

    @Autowired
    DishMapper mapper;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        List<DishTo> newDishes = getNewDishes().stream()
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishes)));

        List<DishTo> dishesWithId = getNewDishesWithId().stream()
                .map(d -> mapper.toDto(d))
                .collect(Collectors.toList());

        assertEquals(JsonUtil.writeValue(dishesWithId), action.andReturn().getResponse().getContentAsString());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = getNewDish();
        DishTo newDishTo = mapper.toDto(newDish);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishTo)));
        DishTo created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, mapper.toDto(newDish));
        DISH_MATCHER.assertMatch(mapper.toDto(dishRepository.getById(newId)), mapper.toDto(newDish));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_3.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mapper.toDto(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(mapper.toDto(dishRepository.getById(DISH_3.id())), mapper.toDto(getUpdated()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.findById(RESTAURANT_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

}

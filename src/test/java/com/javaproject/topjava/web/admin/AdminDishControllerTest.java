package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.model.Restaurant;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.javaproject.topjava.web.DishTestData.*;
import static com.javaproject.topjava.web.admin.UserTestData.ADMIN_MAIL;


//class AdminDishControllerTest extends AbstractControllerTest {
//
//    private static final String REST_URL = AdminDishController.REST_URL + '/';
//
//    @Autowired
//    DishRepository dishRepository;
//
//    @Autowired
//    DishMapper mapper;
//
//    @Test
//    @WithUserDetails(value = ADMIN_MAIL)
//    void createWithLocation() throws Exception {
//        List<DishTo> newRestaurantDishes = getNewDishes().stream()
//                .map(d -> mapper.toDto(d))
//                .collect(Collectors.toList());
//        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JsonUtil.writeValue(newRestaurantDishes)));
//
//        List<DishTo> created = Collections.singletonList(DISH_MATCHER.readFromJson(action));
//        List<DishTo> dishes = newRestaurantDishes.stream()
//                .map(d -> d.setId(created.))
//                .collect(Collectors.toList());
//        newRestaurant.setId(newId);
//        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
//        RESTAURANT_MATCHER.assertMatch(repository.getById(newId), newRestaurant);
//    }
//
//}
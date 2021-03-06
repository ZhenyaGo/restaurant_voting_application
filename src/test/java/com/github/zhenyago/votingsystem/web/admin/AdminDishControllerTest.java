package com.github.zhenyago.votingsystem.web.admin;

import com.github.zhenyago.votingsystem.mapper.DishMapper;
import com.github.zhenyago.votingsystem.model.Dish;
import com.github.zhenyago.votingsystem.repository.DishRepository;
import com.github.zhenyago.votingsystem.to.DishTo;
import com.github.zhenyago.votingsystem.util.JsonUtil;
import com.github.zhenyago.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.zhenyago.votingsystem.web.data.DishTestData.*;
import static com.github.zhenyago.votingsystem.web.data.DishTestData.getUpdated;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;
import static com.github.zhenyago.votingsystem.web.data.UserTestData.ADMIN_MAIL;
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

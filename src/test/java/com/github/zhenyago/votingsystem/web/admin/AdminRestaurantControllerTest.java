package com.github.zhenyago.votingsystem.web.admin;

import com.github.zhenyago.votingsystem.mapper.RestaurantMapper;
import com.github.zhenyago.votingsystem.model.Restaurant;
import com.github.zhenyago.votingsystem.repository.RestaurantRepository;
import com.github.zhenyago.votingsystem.to.RestaurantTo;
import com.github.zhenyago.votingsystem.util.JsonUtil;
import com.github.zhenyago.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.zhenyago.votingsystem.web.data.UserTestData.*;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.getNew;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.NOT_FOUND;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.getUpdated;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminRestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/';

    @Autowired
    RestaurantRepository repository;

    @Autowired
    RestaurantMapper mapper;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mapper.toDto(newRestaurant))));
        RestaurantTo created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, mapper.toDto(newRestaurant));
        RESTAURANT_MATCHER.assertMatch(mapper.toDto(repository.getById(newId)), mapper.toDto(newRestaurant));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_4_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mapper.toDto(updated))))
                .andDo(print())
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(mapper.toDto(repository.getById(RESTAURANT_4_ID)), mapper.toDto(getUpdated()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RESTAURANT_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Restaurant invalid = new Restaurant(null, null, "some address");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mapper.toDto(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Restaurant invalid = new Restaurant(RESTAURANT_1);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(mapper.toDto(invalid))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
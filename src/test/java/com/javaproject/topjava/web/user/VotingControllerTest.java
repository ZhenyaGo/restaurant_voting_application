package com.javaproject.topjava.web.user;


import com.javaproject.topjava.repository.VotingRepository;
import com.javaproject.topjava.to.VotingTo;
import com.javaproject.topjava.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.javaproject.topjava.util.VotingUtil.*;
import static com.javaproject.topjava.web.data.UserTestData.USER_MAIL;
import static com.javaproject.topjava.web.data.RestaurantTestData.*;
import static com.javaproject.topjava.web.data.VotingTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class VotingControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VotingController.REST_URL + '/';

    @Autowired
    VotingRepository repository;


    @Test
    @WithUserDetails(value = USER_MAIL)
    void createVote() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/" + RESTAURANT_3_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        VotingTo result = VOTING_MATCHER.readFromJson(action);
        int newId = result.id();
        VOTING_MATCHER.assertMatch(result, createVotingTo(NEW_VOTE));
        VOTING_MATCHER.assertMatch(createVotingTo(repository.getById(newId)), createVotingTo(NEW_VOTE));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllUserVotes() throws Exception {
        List<VotingTo> userVotes = createVotingTos(getUserVotes());
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTING_MATCHER.contentJson(userVotes));
    }
}
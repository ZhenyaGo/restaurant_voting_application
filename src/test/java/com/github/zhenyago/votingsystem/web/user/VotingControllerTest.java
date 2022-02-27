package com.github.zhenyago.votingsystem.web.user;

import com.github.zhenyago.votingsystem.mapper.VotingMapper;
import com.github.zhenyago.votingsystem.model.Voting;
import com.github.zhenyago.votingsystem.repository.VotingRepository;
import com.github.zhenyago.votingsystem.to.VotingTo;
import com.github.zhenyago.votingsystem.util.JsonUtil;
import com.github.zhenyago.votingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.zhenyago.votingsystem.web.data.UserTestData.USER_MAIL;
import static com.github.zhenyago.votingsystem.web.data.VotingTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VotingControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VotingController.REST_URL + '/';

    @Autowired
    VotingRepository repository;

    @Autowired
    VotingMapper mapper;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createVote() throws Exception {
        Voting newVoting = getNewVote();
        VotingTo newVotingTo = mapper.toDto(newVoting);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVotingTo)));

        VotingTo result = VOTING_MATCHER.readFromJson(action);
        int newId = result.id();
        newVoting.setId(newId);
        VOTING_MATCHER.assertMatch(result, mapper.toDto(newVoting));
        VOTING_MATCHER.assertMatch(mapper.toDto(repository.getById(newId)), mapper.toDto(newVoting));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllUserVotes() throws Exception {
        List<VotingTo> userVotes = getUserVotes().stream()
                .map(v -> mapper.toDto(v)).collect(Collectors.toList());
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTING_MATCHER.contentJson(userVotes));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        VotingTo votingTo = mapper.toDto(VOTE_1);
        perform(MockMvcRequestBuilders.get(REST_URL + VOTE_1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTING_MATCHER.contentJson(votingTo));
    }
}
package com.javaproject.topjava.web.data;


import com.javaproject.topjava.model.Voting;
import com.javaproject.topjava.to.VotingTo;
import com.javaproject.topjava.web.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.javaproject.topjava.web.data.UserTestData.*;
import static com.javaproject.topjava.web.data.RestaurantTestData.*;


public class VotingTestData {
    public static final MatcherFactory.Matcher<VotingTo> VOTING_MATCHER = MatcherFactory.usingEqualsComparator(VotingTo.class);

    public static final Voting NEW_VOTE = new Voting(4, user, RESTAURANT_3);

    public static final Voting VOTE_1 = new Voting(1, user, RESTAURANT_2,
            LocalDate.of(2021, 12, 5), LocalTime.of(10, 0, 0));

    public static final Voting VOTE_2 = new Voting(2, user, RESTAURANT_4,
            LocalDate.of(2021, 12, 6), LocalTime.of(14, 0, 0));

    public static final Voting VOTE_3 = new Voting(3, user, RESTAURANT_3,
            LocalDate.of(2021, 12, 7), LocalTime.of(12, 0, 0));

    public static List<Voting> getUserVotes() {
        return List.of(VOTE_1, VOTE_2, VOTE_3);
    }
}

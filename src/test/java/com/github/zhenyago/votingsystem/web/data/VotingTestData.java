package com.github.zhenyago.votingsystem.web.data;

import com.github.zhenyago.votingsystem.model.Voting;
import com.github.zhenyago.votingsystem.to.VotingTo;
import com.github.zhenyago.votingsystem.web.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.zhenyago.votingsystem.web.data.UserTestData.*;
import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;

public class VotingTestData {
    public static final MatcherFactory.Matcher<VotingTo> VOTING_MATCHER = MatcherFactory.usingEqualsComparator(VotingTo.class);

    public static Voting getNewVote() {
        return new Voting(null, user, RESTAURANT_3);
    }

    public static final int VOTE_1_ID = 1;
    public static final int VOTE_2_ID = 2;
    public static final int VOTE_3_ID = 3;

    public static final Voting VOTE_1 = new Voting(VOTE_1_ID, user, RESTAURANT_2,
            LocalDate.of(2021, 12, 5), LocalTime.of(10, 0, 0));

    public static final Voting VOTE_2 = new Voting(VOTE_2_ID, user, RESTAURANT_4,
            LocalDate.of(2021, 12, 6), LocalTime.of(14, 0, 0));

    public static final Voting VOTE_3 = new Voting(VOTE_3_ID, user, RESTAURANT_3,
            LocalDate.of(2021, 12, 7), LocalTime.of(12, 0, 0));

    public static List<Voting> getUserVotes() {
        return List.of(VOTE_1, VOTE_2, VOTE_3);
    }
}

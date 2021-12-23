package com.javaproject.topjava.util;

import com.javaproject.topjava.model.Voting;
import com.javaproject.topjava.to.VotingTo;
import java.util.List;
import java.util.stream.Collectors;

public class VotingUtil {

    private VotingUtil() {
    }

    public static List<VotingTo> createVotingTos(List<Voting> voting) {
         return voting.stream()
                 .map(VotingUtil::createVotingTos).collect(Collectors.toList());
    }


    public static VotingTo createVotingTos(Voting voting) {
        return new VotingTo(voting.id(), voting.getUser().getId(), voting.getRestaurant().getId(), voting.getVotingDate());
    }
}

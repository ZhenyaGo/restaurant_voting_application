package com.javaproject.topjava.web.user;

import com.javaproject.topjava.to.VotingTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.model.User;
import com.javaproject.topjava.model.Voting;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.repository.UserRepository;
import com.javaproject.topjava.repository.VotingRepository;
import com.javaproject.topjava.util.exception.NotAllowedException;

import java.time.LocalDate;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;
import static com.javaproject.topjava.util.Util.*;
import static com.javaproject.topjava.util.VotingUtil.*;
import static com.javaproject.topjava.web.SecurityUtil.authId;

import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping(value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VotingController {


    private final RestaurantRepository restRepository;
    private final UserRepository userRepository;
    private final VotingRepository votingRepository;

    static final String REST_URL = "/api/voting";

    public VotingController(RestaurantRepository restRepository, UserRepository userRepository, VotingRepository votingRepository) {
        this.restRepository = restRepository;
        this.userRepository = userRepository;
        this.votingRepository = votingRepository;
    }


    @PostMapping(value = "/restaurants/{id}")
    public void voteFor(@PathVariable int id) {
        log.info("Vote for restaurant with id={}", id);
        User user = userRepository.getById(authId());
        Restaurant restaurant = checkNotFoundWithId(restRepository.getById(id),id);

        LocalDate votingDate = LocalDate.now();
        LocalTime votingTime = LocalTime.now();

        Voting registeredVoting = votingRepository.getByUserIdAndVotingDate(id, votingDate);

        if(registeredVoting == null) {
            Voting voting = new Voting(user, restaurant, votingDate, votingTime);
            votingRepository.save(voting);
        } else {
            if(isBefore(registeredVoting.getVotingTime(), LIMIT_TIME)) {
                Voting newVote = new Voting(user, restaurant, votingDate, votingTime);
                newVote.setId(registeredVoting.id());
                votingRepository.save(newVote);
            } else throw new NotAllowedException("You've already voted.Your vote can't be changed!");
        }
    }


    @GetMapping
    public List<VotingTo> getAllVotes() {
        log.info("get all user's votes, user id={}", authId());
        List<Voting> voting = votingRepository.getAllByUserId(authId());
        return createVotingTos(voting);
    }

}

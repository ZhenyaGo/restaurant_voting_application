package com.javaproject.topjava.web.user;

import com.javaproject.topjava.to.VotingTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.model.User;
import com.javaproject.topjava.model.Voting;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.repository.UserRepository;
import com.javaproject.topjava.repository.VotingRepository;
import com.javaproject.topjava.error.NotAllowedException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;
import static com.javaproject.topjava.util.Util.*;
import static com.javaproject.topjava.util.VotingUtil.*;
import static com.javaproject.topjava.web.SecurityUtil.authId;
import java.util.List;


@RestController
@RequestMapping(value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "voting")
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
    @CacheEvict(allEntries = true)
    public ResponseEntity<VotingTo> createVote(@PathVariable int id) {
        log.info("Vote for restaurant with id={}", id);
        User user = userRepository.getById(authId());
        Restaurant restaurant = checkNotFoundWithId(restRepository.getById(id),id);

        LocalDate votingDate = LocalDate.now();
        Voting registeredVoting = votingRepository.getByUserIdAndVotingDate(authId(), votingDate);

        if(registeredVoting == null) {
            Voting voting = new Voting(user, restaurant);
            VotingTo votingTo = createVotingTo(votingRepository.save(voting));
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(votingTo.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(votingTo);
        } else {
            if(isBefore(registeredVoting.getVotingTime(), LIMIT_TIME)) {
                Voting newVote = new Voting(user, restaurant);
                newVote.setId(registeredVoting.id());
                VotingTo newVoteTo = createVotingTo(votingRepository.save(newVote));

                URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(REST_URL + "/{id}")
                        .buildAndExpand(newVoteTo.getId()).toUri();
                return ResponseEntity.created(uriOfNewResource).body(newVoteTo);
            } else throw new NotAllowedException("You've already voted.Your vote can't be changed!");
        }
    }


    @GetMapping
    @Cacheable
    public List<VotingTo> getAllUserVotes() {
        log.info("get all user's votes, user id={}", authId());
        List<Voting> voting = votingRepository.getAllByUserId(authId());
        return createVotingTos(voting);
    }
}

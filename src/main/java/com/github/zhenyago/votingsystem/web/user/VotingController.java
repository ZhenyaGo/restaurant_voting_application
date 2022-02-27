package com.github.zhenyago.votingsystem.web.user;

import com.github.zhenyago.votingsystem.error.NotFoundException;
import com.github.zhenyago.votingsystem.mapper.VotingMapper;
import com.github.zhenyago.votingsystem.to.VotingTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.github.zhenyago.votingsystem.model.Restaurant;
import com.github.zhenyago.votingsystem.model.Voting;
import com.github.zhenyago.votingsystem.repository.RestaurantRepository;
import com.github.zhenyago.votingsystem.repository.VotingRepository;
import com.github.zhenyago.votingsystem.error.NotAllowedException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import static com.github.zhenyago.votingsystem.util.validation.ValidationUtil.*;
import static com.github.zhenyago.votingsystem.util.Util.*;
import static com.github.zhenyago.votingsystem.web.SecurityUtil.authId;
import static com.github.zhenyago.votingsystem.web.SecurityUtil.authUser;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RestController
@RequestMapping(value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "votes")
public class VotingController {

    private final RestaurantRepository restRepository;
    private final VotingRepository votingRepository;
    private final VotingMapper mapper;

    static final String REST_URL = "/api/votes";

    public VotingController(RestaurantRepository restRepository,VotingRepository votingRepository, VotingMapper mapper) {
        this.restRepository = restRepository;
        this.votingRepository = votingRepository;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VotingTo> createWithLocation(@Valid @RequestBody VotingTo voting) {
        checkNew(voting);
        int restaurant_id = voting.getRestaurant_id();
        log.info("Vote for restaurant with id={}", restaurant_id);
        Restaurant restaurant = restRepository.findById(restaurant_id)
                        .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurant_id + " not found"));
        Voting registeredVoting = votingRepository.getByUserIdAndVotingDate(authId(), LocalDate.now());
        if(registeredVoting == null) {
            Voting newVote = new Voting(authUser(), restaurant);
            VotingTo votingTo = mapper.toDto(votingRepository.save(newVote));
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(votingTo.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(votingTo);
        } else throw new NotAllowedException("You've already voted!");
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable int id, @Valid @RequestBody VotingTo voting) {
        log.info("update {} with id={}", voting, id);
        assureIdConsistent(voting, id);
        Voting userVote = votingRepository.getByUserIdAndVotingDate(authId(), LocalDate.now());
        if(userVote != null) {
            if (isBeforeDeadline(LocalTime.now())) {
                int restaurant_id = voting.getRestaurant_id();
                Restaurant restaurant = restRepository.findById(restaurant_id)
                        .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurant_id + " not found"));
                userVote.setRestaurant(restaurant);
                votingRepository.save(userVote);
            } else throw new NotAllowedException("You can't change your vote!");
        } else throw new NotAllowedException("You haven't voted yet today");
    }

    @GetMapping
    public List<VotingTo> getAllUserVotes() {
        log.info("get all user's votes, user id={}", authId());
        List<Voting> voting = votingRepository.getAllByUserId(authId());
        return voting.stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public VotingTo get(@PathVariable int id) {
        log.info("get a vote {}, user id={}", id, authId());
        return mapper.toDto(votingRepository.getByIdAndUserId(id, authId()));
    }
}

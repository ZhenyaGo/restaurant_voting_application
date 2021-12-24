package com.javaproject.topjava.web;

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
import java.time.LocalTime;
import java.util.List;


@RestController
@RequestMapping(value = VotingController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VotingController {

    static final LocalTime LOCAL_TIME = LocalTime.of(11,0, 0);

    private final RestaurantRepository restRepository;
    private final UserRepository userRepository;
    private final VotingRepository votingRepository;

    static final String REST_URL = "/api/voting";

    public VotingController(RestaurantRepository restRepository, UserRepository userRepository, VotingRepository votingRepository) {
        this.restRepository = restRepository;
        this.userRepository = userRepository;
        this.votingRepository = votingRepository;
    }


    @PostMapping(value = "/{id}/restaurant/{restaurant_id}")
    public void voteFor(@PathVariable int id, @PathVariable int restaurant_id) {
        log.info("Vote for restaurant with id={}", id);
        User user = checkNotFoundWithId(userRepository.getById(id), id);
        Restaurant restaurant = checkNotFoundWithId(restRepository.getById(restaurant_id),restaurant_id);

        LocalDate votingDate = LocalDate.now();
        LocalTime votingTime = LocalTime.now();
        //Сначала проверяем проголосовал ли данный пользователь сегодня(находим по id и дате)
        Voting registeredVoting = votingRepository.getByUserIdAndVotingDate(id, votingDate);
        //если нет, тогда регистрируем голос от пользователя
        if(registeredVoting == null) {
            Voting voting = new Voting(user, restaurant, votingDate, votingTime);
            votingRepository.save(voting);
        } else {  //если да, смотрим в какое время был сделан голос, есть ли возможность его изменить
            if(isBefore(registeredVoting.getVotingTime(), LOCAL_TIME)) {
//                votingRepository.deleteExisted(registeredVoting.id());
                Voting newVote = new Voting(user, restaurant, votingDate, votingTime);
                newVote.setId(registeredVoting.id());
                votingRepository.save(newVote);
            } else throw new NotAllowedException("You've already voted.Your vote can't be changed!");
        }
    }


    //Просмотр своих голосов
    @GetMapping(value = "/{id}")
    public List<VotingTo> getAllVotes(@PathVariable int id) {
        log.info("get all user's votes, user id={}", id);
        List<Voting> voting = votingRepository.getAllByUserId(id);
        return createVotingTos(voting);
    }

}

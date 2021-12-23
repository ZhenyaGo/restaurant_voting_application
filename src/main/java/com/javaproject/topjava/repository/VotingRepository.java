package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Voting;

import java.time.LocalDate;
import java.util.List;


@Transactional(readOnly = true)
public interface VotingRepository extends BaseRepository<Voting> {

    Voting getByUserIdAndVotingDate(int userId, LocalDate votingDate);

    List<Voting> getAllByUserId(int userId);

}
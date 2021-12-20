package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Voting;

import java.time.LocalDate;


@Transactional(readOnly = true)
public interface VotingRepository extends BaseRepository<Voting> {

    Voting getByUserIdAndVotingDate(int userId, LocalDate votingDate);

}
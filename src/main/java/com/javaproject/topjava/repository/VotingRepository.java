package com.javaproject.topjava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Voting;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VotingRepository extends BaseRepository<Voting> {

    @Query("SELECT v FROM Voting v WHERE v.user.id=:userId AND v.votingDate=:votingDate")
    Voting getByUserIdAndVotingDate(int userId, LocalDate votingDate);

    @Query("SELECT v FROM Voting v WHERE v.user.id=:userId")
    List<Voting> getAllByUserId(int userId);

    @Query("SELECT v FROM Voting v WHERE v.id=:id AND v.user.id=:userId")
    Voting getByIdAndUserId(int id, int userId);
}
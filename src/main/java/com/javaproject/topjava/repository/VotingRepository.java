package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Voting;



@Transactional(readOnly = true)
public interface VotingRepository extends BaseRepository<Voting> {

}
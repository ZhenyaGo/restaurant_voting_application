package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.User;



@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {


}

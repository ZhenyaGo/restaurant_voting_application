package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Restaurant;

import java.util.Optional;


@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    Optional<Restaurant> getByName(String name);

}

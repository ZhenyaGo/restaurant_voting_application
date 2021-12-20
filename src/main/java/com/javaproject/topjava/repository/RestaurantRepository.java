package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Restaurant;

import java.util.Optional;


@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    Optional<Restaurant> getByName(String name);

//    @EntityGraph(attributePaths = {"menu"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT r FROM Restaurant r WHERE r.id=:id")
//    Restaurant getWithDishes(int id);

}

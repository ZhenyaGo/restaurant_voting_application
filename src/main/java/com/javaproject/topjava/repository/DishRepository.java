package com.javaproject.topjava.repository;

import com.javaproject.topjava.model.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r where d.id=:id")
    Dish getById(int id);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r WHERE d.restaurant.id=:restaurantId")
    List<Dish> getAllRestaurantDishes(int restaurantId, Sort by);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r WHERE d.restaurant.id=:restaurantId AND d.registered=:registered")
    List<Dish> getAllRestaurantDishesByDate(Integer restaurantId, LocalDate registered, Sort by);

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r WHERE d.registered=:registered ORDER BY d.restaurant.id")
    List<Dish> getAllByRegistered(@Param("registered") LocalDate registered);


}

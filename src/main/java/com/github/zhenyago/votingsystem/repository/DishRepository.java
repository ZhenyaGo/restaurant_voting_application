package com.github.zhenyago.votingsystem.repository;

import com.github.zhenyago.votingsystem.model.Dish;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId")
    List<Dish> getAllRestaurantDishes(int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.menu_date=:menu_date")
    List<Dish> getAllRestaurantDishesByDate(Integer restaurantId, LocalDate menu_date, Sort by);

    @Query("SELECT COUNT (d) FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.menu_date=:menu_date")
    int count(int restaurantId, LocalDate menu_date);
}

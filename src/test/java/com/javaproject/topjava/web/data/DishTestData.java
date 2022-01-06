package com.javaproject.topjava.web.data;

import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.javaproject.topjava.web.data.RestaurantTestData.*;

public class DishTestData {

    public static final MatcherFactory.Matcher<DishTo> DISH_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);


    public static final Dish DISH_1 = new Dish(1, "Pasta Carbonara", 450, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));
    public static final Dish DISH_2 = new Dish(2, "Oysters", 700, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));
    public static final Dish DISH_3 = new Dish(3, "Fried eggs", 200, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));


    public static List<Dish> getNewDishes() {
        return List.of(
                new Dish(null, "Wine", 300, RESTAURANT_1, LocalDate.now()),
                new Dish(null, "Pizza Margarita", 600, RESTAURANT_1, LocalDate.now()),
                new Dish(null, "Pasta with pesto", 500, RESTAURANT_1, LocalDate.now()));

    }

    public static Dish getNewDish() {
        return new Dish(null, "Pizza Margarita", 600, RESTAURANT_2, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(3, "Burger", 400, RESTAURANT_1, LocalDate.now());
    }


    public static List<Dish> getNewDishesWithId() {
        return List.of(
                new Dish(13, "Wine", 300, RESTAURANT_1, LocalDate.now()),
                new Dish(14, "Pizza Margarita", 600, RESTAURANT_1, LocalDate.now()),
                new Dish(15, "Pasta with pesto", 500, RESTAURANT_1, LocalDate.now()));
    }
}

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

    public static final Dish DISH_13 = new Dish(13, "Vegetable soup", 325, RESTAURANT_1, LocalDate.now());
    public static final Dish DISH_14 = new Dish(14, "Greek salad", 400, RESTAURANT_1, LocalDate.now());
    public static final Dish DISH_15 = new Dish(15, "Wine", 350, RESTAURANT_1, LocalDate.now());


    public static List<Dish> getNewDishes() {
        return List.of(
                new Dish(null, "Wine", 300, RESTAURANT_2, LocalDate.now()),
                new Dish(null, "Pizza Margarita", 600, RESTAURANT_2, LocalDate.now()),
                new Dish(null, "Pasta with pesto", 500, RESTAURANT_2, LocalDate.now()));

    }

    public static Dish getNewDish() {
        return new Dish(null, "Pizza Margarita", 600, RESTAURANT_2, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(3, "Burger", 400, RESTAURANT_1, LocalDate.now());
    }


    public static List<Dish> getNewDishesWithId() {
        return List.of(
                new Dish(19, "Wine", 300, RESTAURANT_2, LocalDate.now()),
                new Dish(20, "Pizza Margarita", 600, RESTAURANT_2, LocalDate.now()),
                new Dish(21, "Pasta with pesto", 500, RESTAURANT_2, LocalDate.now()));
    }

    public static List<Dish> newDishes() {
        return List.of(DISH_13, DISH_14, DISH_15);

    }
}

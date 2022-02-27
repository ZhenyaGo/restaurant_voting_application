package com.github.zhenyago.votingsystem.web.data;

import com.github.zhenyago.votingsystem.model.Dish;
import com.github.zhenyago.votingsystem.to.DishTo;
import com.github.zhenyago.votingsystem.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.zhenyago.votingsystem.web.data.RestaurantTestData.*;

public class DishTestData {

    public static final MatcherFactory.Matcher<DishTo> DISH_MATCHER = MatcherFactory.usingEqualsComparator(DishTo.class);

    public static final Dish DISH_1 = new Dish(1, "Pasta Carbonara", 450, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));
    public static final Dish DISH_2 = new Dish(2, "Oysters", 700, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));
    public static final Dish DISH_3 = new Dish(3, "Fried eggs", 200, RESTAURANT_1 ,LocalDate.of(2021, 12, 1));

    public static final Dish DISH_13 = new Dish(13, "Vegetable soup", 325, RESTAURANT_1, LocalDate.now());
    public static final Dish DISH_14 = new Dish(14, "Greek salad", 400, RESTAURANT_1, LocalDate.now());
    public static final Dish DISH_15 = new Dish(15, "Wine", 350, RESTAURANT_1, LocalDate.now());

    public static Dish getNewDish() {
        return new Dish(null, "Pizza Margarita", 600, RESTAURANT_2, LocalDate.now());
    }

    public static Dish getUpdated() {
        return new Dish(3, "Burger", 400, RESTAURANT_1, LocalDate.now());
    }

    public static List<Dish> newDishes() {
        return List.of(DISH_13, DISH_14, DISH_15);
    }

    public static final Dish DISH_16 = new Dish(16, "Onion rings", 344, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_17 = new Dish(17, "Garlic bread", 250, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_18 = new Dish(18, "Milkshake", 285, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_19 = new Dish(19, "Cheese sticks", 230, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_20 = new Dish(20, "Tuna and egg sandwich", 200, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_21 = new Dish(21, "BBQ ribs", 420, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_22 = new Dish(22, "Spaghetti Bolognese", 365, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_23 = new Dish(23, "Caprese salad", 350, RESTAURANT_3, LocalDate.now());
    public static final Dish DISH_24 = new Dish(24, "Cherry pie", 275, RESTAURANT_3, LocalDate.now());
}

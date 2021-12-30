package com.javaproject.topjava.web;

import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.to.RestaurantTo;


public class RestaurantTestData {
    public static final MatcherFactory.Matcher<RestaurantTo> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(RestaurantTo.class);


    public static final int RESTAURANT_1_ID = 1;
    public static final int RESTAURANT_2_ID = 2;
    public static final int RESTAURANT_3_ID = 3;
    public static final int RESTAURANT_4_ID = 4;
    public static final int NOT_FOUND = 100;


    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_1_ID, "White Rabbit", "Смоленская площадь, 3");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_2_ID, "Selfie", "Новинский бул., 31");
    public static final Restaurant RESTAURANT_3 = new Restaurant(RESTAURANT_3_ID, "Twins Garden", "Страстной бул., 8");
    public static final Restaurant RESTAURANT_4 = new Restaurant(RESTAURANT_4_ID, "Grand Cru", "Малая Бронная ул., 22");

    public static Restaurant getNew() {
        return new Restaurant(null,"Cafe Pushkin", "Тверской бул., 26А");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(4, "New Restaurant", "Малая Бронная ул., 22");
    }
}

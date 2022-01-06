package com.javaproject.topjava.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.to.RestaurantTo;

@Component
public class RestaurantMapper extends AbstractMapper<Restaurant, RestaurantTo> {

    @Autowired
    RestaurantMapper() {
        super(Restaurant.class, RestaurantTo.class);
    }
}

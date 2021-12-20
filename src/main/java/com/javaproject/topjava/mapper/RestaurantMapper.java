package com.javaproject.topjava.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.to.RestaurantTo;

@Component//без этой аннотации Spring не находит Bean
public class RestaurantMapper extends AbstractMapper<Restaurant, RestaurantTo> {

    @Autowired
    RestaurantMapper() {
        super(Restaurant.class, RestaurantTo.class);
    }
}

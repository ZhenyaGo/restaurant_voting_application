package com.github.zhenyago.votingsystem.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.zhenyago.votingsystem.model.Restaurant;
import com.github.zhenyago.votingsystem.to.RestaurantTo;

@Component
public class RestaurantMapper extends AbstractMapper<Restaurant, RestaurantTo> {

    @Autowired
    RestaurantMapper() {
        super(Restaurant.class, RestaurantTo.class);
    }
}

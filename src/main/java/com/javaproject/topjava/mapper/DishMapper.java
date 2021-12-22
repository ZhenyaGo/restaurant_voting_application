package com.javaproject.topjava.mapper;

import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.to.DishTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DishMapper extends AbstractMapper<Dish, DishTo> {

    @Autowired
    DishMapper() {
        super(Dish.class, DishTo.class);
    }
}

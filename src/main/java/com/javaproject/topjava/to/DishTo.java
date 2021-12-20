package com.javaproject.topjava.to;

import org.hibernate.validator.constraints.Range;
import com.javaproject.topjava.model.Restaurant;


public class DishTo extends NamedTo {


    @Range(min = 50, max = 1000)
    Integer price;


    Restaurant restaurant;

    public DishTo(Integer id, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }

    public DishTo() {

    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", price=" + price +
                ", restaurant=" + restaurant +
                ", name='" + name + '\'' +
                '}';
    }
}
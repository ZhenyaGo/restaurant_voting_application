package com.javaproject.topjava.to;

import org.hibernate.validator.constraints.Range;
import com.javaproject.topjava.model.Restaurant;

import java.util.Objects;


public class DishTo extends NamedTo {


    @Range(min = 50, max = 1000)
    Integer price;


    Integer restaurantId;

    public DishTo(Integer id, String name, Integer price, Integer restaurantId) {
        super(id, name);
        this.price = price;
        this.restaurantId = restaurantId;
    }

    public DishTo() {

    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishTo dishTo = (DishTo) o;
        return  Objects.equals(price, dishTo.price) &&
                Objects.equals(restaurantId, dishTo.restaurantId) &&
                Objects.equals(id, dishTo.id) &&
                Objects.equals(name, dishTo.name);

    }

    @Override
    public int hashCode() {
        return Objects.hash(price, restaurantId, id, name);
    }

    @Override
    public String toString() {
        return "DishTo{" +
                "id=" + id +
                ", price=" + price +
                ", restaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                '}';
    }
}
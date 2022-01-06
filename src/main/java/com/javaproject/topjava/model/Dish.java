package com.javaproject.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
//Накладываем ограничение, что у одного ресторана в один день не может быть двух одинаковых позиций в меню.
@Table(name = "dishes",  uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "registered", "name"}, name = "dish_unique_name_idx")})
public class Dish extends NamedEntity {

    @Column(name = "price")
    @Range(min = 50, max = 1000)
    private Integer price;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate registered = LocalDate.now();

    public Dish() {

    }

    public Dish(Integer price, Restaurant restaurant, LocalDate registered) {
        this.price = price;
        this.restaurant = restaurant;
        this.registered = registered;
    }

    public Dish(Integer id, String name, Integer price, Restaurant restaurant, LocalDate registered) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
        this.registered = registered;
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

    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", price=" + price +
                ", registered=" + registered +
                ", name='" + name + '\'' +
                '}';
    }
}

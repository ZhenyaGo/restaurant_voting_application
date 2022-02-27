package com.javaproject.topjava.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Table(name = "menu_item",  uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "menu_date", "name"}, name = "dish_unique_name_idx")})
public class Dish extends NamedEntity {

    @Column(name = "price")
    @Range(max = 100000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private LocalDate menu_date;

    public Dish(Integer id, String name, Integer price, Restaurant restaurant, LocalDate menu_date) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
        this.menu_date = menu_date;
    }
}

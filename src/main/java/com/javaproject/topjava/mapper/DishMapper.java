package com.javaproject.topjava.mapper;

import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.to.DishTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class DishMapper extends AbstractMapper<Dish, DishTo> {

    private final RestaurantRepository repository;

    @Autowired
    DishMapper(RestaurantRepository repository) {
        super(Dish.class, DishTo.class);
        this.repository = repository;
    }


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Dish.class, DishTo.class)
                .addMappings(m -> m.skip(DishTo::setRestaurantId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DishTo.class, Dish.class)
                .addMappings(m -> m.skip(Dish::setRestaurant)).setPostConverter(toEntityConverter());
    }

    private Integer getId(Dish source) {
        return Objects.isNull(source) ? null : source.getRestaurant().getId();
    }

    @Override
    public void mapSpecificFields(Dish source, DishTo destination) {
        destination.setRestaurantId(getId(source));
    }

    @Override
    void mapSpecificFields(DishTo source, Dish destination) {
        destination.setRestaurant(repository.findById(source.getRestaurantId()).orElse(null));
    }
}

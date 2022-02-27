package com.github.zhenyago.votingsystem.mapper;

import com.github.zhenyago.votingsystem.model.Dish;
import com.github.zhenyago.votingsystem.repository.RestaurantRepository;
import com.github.zhenyago.votingsystem.to.DishTo;
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

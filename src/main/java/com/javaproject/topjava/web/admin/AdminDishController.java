package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.util.exception.NotAllowedException;
import com.javaproject.topjava.util.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.util.validation.ValidationUtil;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {

    static final String REST_URL = "api/admin/dishes";


    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper mapper;

    public AdminDishController(DishRepository dishRepository, RestaurantRepository restaurantRepository, DishMapper mapper) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(value = "restaurant/{id}")
    public List<DishTo> create(@PathVariable int id, @RequestBody List<DishTo> menuTo) {
        log.info("create a menu {} for a restaurant with id={}", menuTo, id);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
        List<Dish> actualMenu = dishRepository.getAllRestaurantDishesByDate(id, LocalDate.now(), Sort.by("id"));
        if (actualMenu.isEmpty()) {
            menuTo.forEach(ValidationUtil::checkNew);
            menuTo.forEach(m -> m.setRestaurant(restaurant));
            List<Dish> menu = menuTo.stream()
                    .map(mapper::toEntity).collect(Collectors.toList());
            return dishRepository.saveAll(menu).stream()
                    .map(mapper::toDto).collect(Collectors.toList());
        } else throw new NotAllowedException("Today's menu has been already created.");
    }



    @PutMapping(value = "/{id}/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable int id, @Valid @RequestBody DishTo dishTo, @PathVariable int restaurant_id) {
        log.info("update a dish {} with id={} for a restaurant with id={}", dishTo, id, restaurant_id);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(restaurant_id).orElse(null), restaurant_id);
        Dish dish = checkNotFoundWithId(dishRepository.getById(id), id);
        if(Objects.equals(dish.getRestaurant().getId(), restaurant != null ? restaurant.getId() : null)) {
            assureIdConsistent(dishTo, id);
            dishTo.setRestaurant(restaurant);
            dishRepository.save(mapper.toEntity(dishTo));
        } else throw new NotAllowedException("The dish doesn't belong to this restaurant!");
    }
}

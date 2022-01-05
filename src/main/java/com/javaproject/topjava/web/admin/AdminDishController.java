package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.util.exception.NotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.util.validation.ValidationUtil;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class AdminDishController {

    static final String REST_URL = "/api/admin/dishes";


    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper mapper;

    public AdminDishController(DishRepository dishRepository, RestaurantRepository restaurantRepository, DishMapper mapper) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(value = "all", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public List<DishTo> create(@Valid @RequestBody List<DishTo> dishes) {
        int restaurantId = dishes.get(1).getRestaurantId();
        log.info("create a menu {} for a restaurant with id={}", dishes, restaurantId);
        checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId);
        List<Dish> actualMenu = dishRepository.getAllRestaurantDishesByDate(restaurantId, LocalDate.now(), Sort.by("id"));
        if (actualMenu.isEmpty()) {
            dishes.forEach(ValidationUtil::checkNew);
            List<Dish> menu = dishes.stream()
                    .map(mapper::toEntity).collect(Collectors.toList());
            return dishRepository.saveAll(menu).stream()
                    .map(mapper::toDto).collect(Collectors.toList());
        } else throw new NotAllowedException("Today's menu has been already created.");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody DishTo dishTo) {
        int restaurantId = dishTo.getRestaurantId();
        log.info("create a dish {} for a restaurant with id={}", dishTo, restaurantId);
        checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId);
        List<Dish> actualMenu = dishRepository.getAllRestaurantDishesByDate(restaurantId, LocalDate.now(), Sort.by("id"));
        if (actualMenu.isEmpty()) {
            checkNew(dishTo);
            Dish newDish = dishRepository.save(mapper.toEntity(dishTo));
            DishTo newDishTo = mapper.toDto(newDish);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(newDishTo.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(newDishTo);
        } else throw new NotAllowedException("Today's menu has been already created.");
    }



    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@PathVariable int id, @Valid @RequestBody DishTo dishTo) {
        int restaurantId = dishTo.getRestaurantId();
        log.info("update a dish {} with id={} for a restaurant with id={}", dishTo, id, restaurantId);
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId);
        Dish dish = checkNotFoundWithId(dishRepository.getById(id), id);
        assureIdConsistent(dishTo, id);
        assert restaurant != null;
        if(Objects.equals(dish.getRestaurant().getId(), restaurant.getId())) {
            dishRepository.save(mapper.toEntity(dishTo));
        } else throw new NotAllowedException("The dish doesn't belong to this restaurant!");
    }
}

package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.error.NotAllowedException;
import com.javaproject.topjava.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.repository.RestaurantRepository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody DishTo dishTo) {
        int restaurantId = dishTo.getRestaurantId();
        log.info("create a dish {} for a restaurant with id={}", dishTo, restaurantId);
        checkNotFoundWithId(restaurantRepository.findById(restaurantId).orElse(null), restaurantId);
        int menuSize = dishRepository.count(restaurantId, dishTo.getMenu_date());
        if(menuSize < 5) {
            checkNew(dishTo);
            Dish newDish = dishRepository.save(mapper.toEntity(dishTo));
            DishTo newDishTo = mapper.toDto(newDish);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(newDishTo.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(newDishTo);
        } else throw new NotAllowedException("Today's menu has been already created.");
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@PathVariable int id, @Valid @RequestBody DishTo dishTo) {
        assureIdConsistent(dishTo, id);
        int restaurantId = dishTo.getRestaurantId();
        log.info("update a dish {} with id={} for a restaurant with id={}", dishTo, id, restaurantId);
        Dish dish = dishRepository.getById(id);
        if(Objects.equals(dish.getRestaurant().getId(), restaurantId)) {
            dishRepository.save(mapper.toEntity(dishTo));
        } else throw new NotFoundException("The dish doesn't belong to this restaurant!");
    }
}

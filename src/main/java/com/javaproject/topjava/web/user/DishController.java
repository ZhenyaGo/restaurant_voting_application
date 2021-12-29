package com.javaproject.topjava.web.user;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.to.DishTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "dishes")
public class DishController {

    private static final Sort SORT_ID = Sort.by(Sort.Direction.ASC, "id");

    private final DishRepository dishRepository;
    private final DishMapper mapper;

    public DishController(DishRepository dishRepository, DishMapper mapper) {
        this.dishRepository = dishRepository;
        this.mapper = mapper;
    }

    static final String REST_URL = "api/dishes";

    //получение списка еды конкретного ресторана за определенную дату(сортировка по id)
    //(если localDate=null отображается еда за сегодня)
    @GetMapping(value = "/restaurant/{id}")
    public List<DishTo> getAll(@PathVariable int id,
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate localDate) {
        log.info("get the restaurant menu with id={}", id);
        return localDate == null
                ? dishRepository.getAllRestaurantDishesByDate(id, LocalDate.now(), SORT_ID).stream()
                .map(mapper::toDto).collect(Collectors.toList())
                : dishRepository.getAllRestaurantDishesByDate(id, localDate, SORT_ID).stream()
                .map(mapper::toDto).collect(Collectors.toList());

    }


    //получение списка еды конкретного ресторана за все время
    @GetMapping(value = "/all/restaurant/{id}")
    public List<DishTo> getAllRestaurantDishes(@PathVariable int id) {
        log.info("get restaurant's dishes with id={} for all time.", id);
        return dishRepository.getAllRestaurantDishes(id).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    //получение списка еды всех ресторанов за сегодня.
    @GetMapping()
    @Cacheable
    public List<DishTo> getAllDishesForToday() {
        log.info("get all dishes of restaurants for today");
        List<Dish> todayMenu = dishRepository.getAllByRegistered(LocalDate.now());
        if(todayMenu.isEmpty()) throw new NotFoundException("The actual menu hasn't been created yet! Try again a bit later!");
        return  todayMenu.stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    //получение еды по id
    @GetMapping(value = "/{id}")
    public DishTo get(@PathVariable int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(mapper.toDto(dishRepository.getById(id)), id);

    }
}
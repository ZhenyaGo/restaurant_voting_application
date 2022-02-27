package com.github.zhenyago.votingsystem.web.user;

import com.github.zhenyago.votingsystem.error.NotFoundException;
import com.github.zhenyago.votingsystem.mapper.DishMapper;
import com.github.zhenyago.votingsystem.to.DishTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import com.github.zhenyago.votingsystem.repository.DishRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    static final String REST_URL = "/api/dishes";

    //get a restaurant's dishes on a certain date
    @GetMapping(value = "/restaurants/{id}/by-date")
    public List<DishTo> getRestaurantDishesByDate(@PathVariable int id,
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam LocalDate date) {
        log.info("get restaurant's dishes with id={} on a certain date.", id);
        Assert.notNull(date, "Date mustn't be null");
        return dishRepository.getAllRestaurantDishesByDate(id, date, SORT_ID).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    //get a restaurant's dishes for today
    @GetMapping(value = "/restaurants/{id}")
    @Cacheable
    public List<DishTo> getRestaurantDishesForToday(@PathVariable int id) {
        log.info("get restaurant's dishes with id={} for today.", id);
        return dishRepository.getAllRestaurantDishesByDate(id, LocalDate.now(), SORT_ID).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    //get a restaurant's dishes for all time
    @GetMapping(value = "/restaurants/{id}/all")
    public List<DishTo> getAllRestaurantDishes(@PathVariable int id) {
        log.info("get restaurant's dishes with id={} for all time.", id);
        return dishRepository.getAllRestaurantDishes(id).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public DishTo get(@PathVariable int id) {
        log.info("get {}", id);
        return mapper.toDto(dishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu item with id=" + id + " not found")));
    }
}
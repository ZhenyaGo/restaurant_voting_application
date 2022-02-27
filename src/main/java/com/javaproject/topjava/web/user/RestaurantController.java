package com.javaproject.topjava.web.user;

import com.javaproject.topjava.mapper.RestaurantMapper;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.to.RestaurantTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public RestaurantController(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get {}", id);
        Restaurant restaurant = checkNotFoundWithId(repository.findById(id).orElse(null), id);
        return mapper.toDto(restaurant);
    }

    @GetMapping(value = "/by-name")
    public RestaurantTo getByName(@RequestParam String name) {
        log.info("getByName {}", name);
        Restaurant restaurant = checkNotFound(repository.getByName(name).orElse(null), "name=" + name);
        return mapper.toDto(restaurant);
    }

    @GetMapping(value = "/with-menu")
    @Cacheable
    public List<Restaurant> getWithMenu() {
        log.info("get restaurants with menu for today");
        return repository.getRestaurantsWithMenuForToday(LocalDate.now());
    }
}

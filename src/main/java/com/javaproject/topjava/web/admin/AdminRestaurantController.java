package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.RestaurantMapper;
import com.javaproject.topjava.to.RestaurantTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;


import static com.javaproject.topjava.util.validation.ValidationUtil.*;


@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "restaurants")
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public AdminRestaurantController(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant newRestaurant = repository.save(mapper.toEntity(restaurant));
        RestaurantTo newRestaurantTo = mapper.toDto(newRestaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(newRestaurantTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newRestaurantTo);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody RestaurantTo restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        //проверяем существует ли ресторан, который мы хотим обновить
        checkNotFoundWithId(repository.findById(id).orElse(null), id);
        assureIdConsistent(restaurant, id);
        repository.save(mapper.toEntity(restaurant));
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

}

package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.RestaurantMapper;
import com.javaproject.topjava.to.RestaurantTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;
import static com.javaproject.topjava.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;
    private final RestaurantMapper mapper;

    public AdminRestaurantController(RestaurantRepository repository, RestaurantMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<RestaurantTo> createWithLocation(@RequestBody RestaurantTo restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant newRestaurant = repository.save(mapper.toEntity(restaurant));
        RestaurantTo newRestaurantTo = mapper.toDto(newRestaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(newRestaurantTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newRestaurantTo);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody RestaurantTo restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        //проверяем существует ли ресторан, который мы хотим обновить
        checkNotFoundWithId(repository.findById(id).orElse(null), id);
        assureIdConsistent(restaurant, id);
        repository.save(mapper.toEntity(restaurant));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
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

}

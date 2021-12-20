package com.javaproject.topjava.web.admin;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;

    public AdminRestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant newRestaurant = repository.save(restaurant);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(newRestaurant.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newRestaurant);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int id) {
        assureIdConsistent(restaurant, id);
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "address"));
    }


    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        repository.deleteExisted(id);
    }

    @GetMapping(value = "/{id}")
    public Restaurant get(@PathVariable int id) {
        return repository.findById(id).orElse(null);
    }

    @GetMapping(value = "/by-name")
    public ResponseEntity<Restaurant> getByName(@RequestParam String name) {
        return ResponseEntity.of(
                checkNotFound(repository.getByName(name), "name=" + name));
    }

    //    @GetMapping(value = "/{id}/with-dishes")
//    public Restaurant getWithDishes(@PathVariable int id) {
//        return repository.getWithDishes(id);
//    }
}

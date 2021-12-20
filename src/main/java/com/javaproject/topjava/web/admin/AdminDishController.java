package com.javaproject.topjava.web.admin;

import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.util.validation.ValidationUtil;


import java.time.LocalDate;
import java.util.List;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "api/admin/dishes";//четкое разделение ролей на основе URL

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public AdminDishController(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        dishRepository.deleteExisted(id);
    }

    @PostMapping(value = "restaurant/{id}")
    public List<Dish> create(@PathVariable int id, @RequestBody List<Dish> menu) {
        Restaurant restaurant = restaurantRepository.findById(id).orElse(null);
        List<Dish> actualMenu = dishRepository.getAllRestaurantDishesByDate(id, LocalDate.now(), Sort.by("id"));
        if (actualMenu.isEmpty()) {
            menu.forEach(ValidationUtil::checkNew);
            menu.forEach(m -> m.setRestaurant(restaurant));
            return dishRepository.saveAll(menu);
        } else return actualMenu;
    }


    @PutMapping(value = "/{id}/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable int id, @RequestBody Dish dish, @PathVariable int restaurant_id) {
        Restaurant restaurant = restaurantRepository.findById(restaurant_id).orElse(null);
        assureIdConsistent(dish, id);
        dish.setRestaurant(restaurant);
        checkNotFoundWithId(dishRepository.save(dish), dish.id());
    }
}

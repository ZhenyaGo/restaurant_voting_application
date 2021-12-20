package com.javaproject.topjava.web.user;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {

    private static final Sort SORT_REGISTERED = Sort.by(Sort.Direction.DESC, "registered");
    private static final Sort SORT_ID = Sort.by(Sort.Direction.ASC, "id");

    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    static final String REST_URL = "api/profile/dishes";

    //получение списка еды конкретного ресторана за все время(сортировка по дате регистрации еды)
    //получение списка еды конкретного ресторана за определенную дату(сортировка по id)
    @GetMapping(value = "/restaurant/{id}")
    public List<Dish> getAll(@PathVariable int id,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate localDate) {
        return localDate == null
                ? dishRepository.getAllRestaurantDishes(id, SORT_REGISTERED)
                : dishRepository.getAllRestaurantDishesByDate(id, localDate, SORT_ID);

    }

    //получение списка еды всех ресторанов за сегодня.
    @GetMapping()
    public List<Dish> getAllDishesForToday() {
        List<Dish> todayMenu = dishRepository.getAllByRegistered(LocalDate.now());
        if(todayMenu.isEmpty()) throw new NotFoundException("The actual menu hasn't been created yet! Try again a bit later!");
        return  todayMenu;
    }

    //получение еды по id
    @GetMapping(value = "/{id}")
    public Dish get(@PathVariable int id) {
        return checkNotFoundWithId(dishRepository.getById(id), id);

    }
}
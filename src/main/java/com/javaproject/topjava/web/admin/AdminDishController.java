package com.javaproject.topjava.web.admin;

import com.javaproject.topjava.mapper.DishMapper;
import com.javaproject.topjava.to.DishTo;
import com.javaproject.topjava.util.exception.NotAllowedException;
import com.javaproject.topjava.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.javaproject.topjava.model.Dish;
import com.javaproject.topjava.model.Restaurant;
import com.javaproject.topjava.repository.DishRepository;
import com.javaproject.topjava.repository.RestaurantRepository;
import com.javaproject.topjava.util.validation.ValidationUtil;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.javaproject.topjava.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDishController {

    static final String REST_URL = "api/admin/dishes";

    private static final Sort SORT_ID = Sort.by(Sort.Direction.ASC, "id");

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper mapper;

    public AdminDishController(DishRepository dishRepository, RestaurantRepository restaurantRepository, DishMapper mapper) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable int id) {
        dishRepository.deleteExisted(id);
    }

    @PostMapping(value = "restaurant/{id}")
    public List<DishTo> create(@PathVariable int id, @RequestBody List<DishTo> menuTo) {
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(id).orElse(null), id);
        List<Dish> actualMenu = dishRepository.getAllRestaurantDishesByDate(id, LocalDate.now(), Sort.by("id"));
        if (actualMenu.isEmpty()) {
            menuTo.forEach(ValidationUtil::checkNew);
            menuTo.forEach(m -> m.setRestaurant(restaurant));
            List<Dish> menu = menuTo.stream()
                    .map(mapper::toEntity).collect(Collectors.toList());
            return dishRepository.saveAll(menu).stream()
                    .map(mapper::toDto).collect(Collectors.toList());
        } else throw new NotAllowedException("Today's menu has been already created.");
    }


    //может сделать проще??
    @PutMapping(value = "/{id}/restaurant/{restaurant_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void update(@PathVariable int id, @RequestBody DishTo dishTo, @PathVariable int restaurant_id) {
        //проверяем есть ли ресторан, еду которого мы хотим обновить
        Restaurant restaurant = checkNotFoundWithId(restaurantRepository.findById(restaurant_id).orElse(null), restaurant_id);
        //проверяем существует ли еда, которую мы хотим обновить
        Dish dish = checkNotFoundWithId(dishRepository.getById(id), id);
        //принадлежит ли еда данному ресторану
        if(Objects.equals(dish.getRestaurant().getId(), restaurant != null ? restaurant.getId() : null)) {
            assureIdConsistent(dishTo, id);
            dishTo.setRestaurant(restaurant);
            dishRepository.save(mapper.toEntity(dishTo));
        } else throw new NotAllowedException("The dish doesn't belong to this restaurant!");
    }


    //получение списка еды конкретного ресторана за все время
    //получение списка еды конкретного ресторана за определенную дату(сортировка по id)
    @GetMapping(value = "/restaurant/{id}")
    public List<DishTo> getAll(@PathVariable int id,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(required = false) LocalDate localDate) {
        return localDate == null
                ? dishRepository.getAllRestaurantDishes(id).stream()
                .map(mapper::toDto).collect(Collectors.toList())
                : dishRepository.getAllRestaurantDishesByDate(id, localDate, SORT_ID).stream()
                .map(mapper::toDto).collect(Collectors.toList());

    }

    //получение списка еды всех ресторанов за сегодня.
    @GetMapping()
    public List<DishTo> getAllDishesForToday() {
        List<Dish> todayMenu = dishRepository.getAllByRegistered(LocalDate.now());
        if(todayMenu.isEmpty()) throw new NotFoundException("The actual menu hasn't been created yet! Try again a bit later!");
        return  todayMenu.stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    //получение еды по id
    @GetMapping(value = "/{id}")
    public DishTo get(@PathVariable int id) {
        return checkNotFoundWithId(mapper.toDto(dishRepository.getById(id)), id);

    }
}

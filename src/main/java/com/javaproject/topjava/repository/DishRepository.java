package com.javaproject.topjava.repository;

import com.javaproject.topjava.model.Dish;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

}

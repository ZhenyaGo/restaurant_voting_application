package com.javaproject.topjava.repository;

import org.springframework.transaction.annotation.Transactional;
import com.javaproject.topjava.model.Restaurant;



@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {


}

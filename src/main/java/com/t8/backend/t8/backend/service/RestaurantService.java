package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.RestaurantDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    RestaurantDto register(RestaurantDto dto);
    RestaurantDto getById(Long id);
    List<RestaurantDto> getAll();
    RestaurantDto update(Long id, RestaurantDto dto);
    void delete(Long id);
}
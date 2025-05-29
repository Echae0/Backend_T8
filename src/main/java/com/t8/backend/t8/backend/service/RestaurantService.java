package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.RestaurantDto;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private Restaurant toEntity(RestaurantDto dto) {
        // 엔티티 변환 구현 필요
        return null;
    }

    private RestaurantDto toDto(Restaurant entity) {
        // DTO 변환 구현 필요
        return null;
    }

    @Transactional
    public RestaurantDto register(RestaurantDto dto) {
        Restaurant restaurant = toEntity(dto);
        return toDto(restaurantRepository.save(restaurant));
    }

    public RestaurantDto getById(Long id) {
        return restaurantRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));
    }

    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDto update(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));

        // 업데이트 로직 구현 필요

        return toDto(restaurantRepository.save(restaurant));
    }

    @Transactional
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}

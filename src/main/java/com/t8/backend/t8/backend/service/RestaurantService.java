package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.RestaurantDto;
import com.t8.backend.t8.backend.entity.Category;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    private Restaurant toEntity(RestaurantDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId()));

        return Restaurant.builder()
                .restaurantName(dto.getRestaurantName())
                .location(dto.getLocation())
                .imageUrl(dto.getImageUrl())
                .category(category)
                .contactNumber(dto.getContactNumber())
                .openingHours(dto.getOpeningHours())
                .build();
    }

    private RestaurantDto toDto(Restaurant entity) {
        return RestaurantDto.builder()
                .id(entity.getId())
                .restaurantName(entity.getRestaurantName())
                .location(entity.getLocation())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .category(entity.getCategory() != null ? entity.getCategory().getCategoryName() : null)
                .contactNumber(entity.getContactNumber())
                .openingHours(entity.getOpeningHours())
                .build();
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
        return restaurantRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RestaurantDto update(Long id, RestaurantDto dto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + id));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + dto.getCategoryId()));

        restaurant.setRestaurantName(dto.getRestaurantName());
        restaurant.setLocation(dto.getLocation());
        restaurant.setImageUrl(dto.getImageUrl());
        restaurant.setCategory(category);
        restaurant.setContactNumber(dto.getContactNumber());
        restaurant.setOpeningHours(dto.getOpeningHours());

        return toDto(restaurant);
    }

    @Transactional
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}

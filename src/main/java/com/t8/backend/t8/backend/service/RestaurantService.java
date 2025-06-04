package com.t8.backend.t8.backend.service;

import com.t8.backend.t8.backend.dto.CategoryDto;
import com.t8.backend.t8.backend.dto.RestaurantDto;
import com.t8.backend.t8.backend.entity.Category;
import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.entity.Review;
import com.t8.backend.t8.backend.repository.CategoryRepository;
import com.t8.backend.t8.backend.repository.ReservationRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final ReservationRepository reservationRepository;


    private Restaurant toEntity(RestaurantDto dto) {
//        Category category = Optional.ofNullable(dto.getCategory())
//                .map(catDto -> Category.builder()
//                        .id(catDto.getId())
//                        .categoryCode(catDto.getCategoryCode())
//                        .categoryName(catDto.getCategoryName())
//                        .build())
//                .orElseThrow(() -> new IllegalArgumentException("Category is required"));

        Category category = categoryRepository.findByCategoryCode(dto.getCategoryCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category code: " + dto.getCategoryCode()));

        return Restaurant.builder()
                .restaurantName(dto.getRestaurantName())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .parking(dto.getParking())
                .imageUrl(dto.getImageUrl())
                .category(category)
                .contactNumber(dto.getContactNumber())
                .openingHours(dto.getOpeningHours())
                .averageRating(dto.getAverageRating() != null ? dto.getAverageRating() : 0.0)
                .dailyLimitedTeams(dto.getDailyLimitedTeams())
                .availableTeams(dto.getAvailableTeams())
                .CurrentWaitingTeams(dto.getCurrentWaitingTeams() != null ? dto.getCurrentWaitingTeams() : 0)
                .PredictedWaitingTime(dto.getPredictedWaitingTime() != null ? dto.getPredictedWaitingTime() : 0)
                .build();    }

    private RestaurantDto toDto(Restaurant entity) {

        int reservationCount = reservationRepository.findActiveByRestaurantId(entity.getId()).size();
        int predicted = reservationCount * getRandomMultiplier();

        return RestaurantDto.builder()
                .id(entity.getId())
                .restaurantName(entity.getRestaurantName())
                .location(entity.getLocation())
                .description(entity.getDescription())
                .parking(entity.getParking())
                .imageUrl(entity.getImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
//                .category(entity.getCategory() != null ? CategoryDto.builder()
//                        .id(entity.getCategory().getId())
//                        .categoryCode(entity.getCategory().getCategoryCode())
//                        .categoryName(entity.getCategory().getCategoryName())
//                        .build() : null)
                .categoryCode(entity.getCategory() != null ? entity.getCategory().getCategoryCode() : null)
                .contactNumber(entity.getContactNumber())
                .openingHours(entity.getOpeningHours())
                .averageRating(entity.getAverageRating())
                .dailyLimitedTeams(entity.getDailyLimitedTeams())
                .availableTeams(entity.getAvailableTeams())
                .CurrentWaitingTeams(entity.getCurrentWaitingTeams())
                .PredictedWaitingTime(entity.getPredictedWaitingTime())
                .CurrentWaitingTeams(reservationCount)
                .PredictedWaitingTime(predicted)
                .build();
    }

    @Transactional
    public RestaurantDto create(RestaurantDto dto) {
        Restaurant restaurant = toEntity(dto);

        Restaurant saved = restaurantRepository.save(restaurant);

        int reservationCount = reservationRepository.findActiveByRestaurantId(saved.getId()).size();
        int predicted = reservationCount * getRandomMultiplier();

        saved.setCurrentWaitingTeams(reservationCount);
        saved.setPredictedWaitingTime(predicted);
        restaurantRepository.save(saved); // 업데이트 반영

        return toDto(saved);
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

//        Category category = Optional.ofNullable(dto.getCategory())
//                .map(catDto -> Category.builder()
//                        .id(catDto.getId())
//                        .categoryCode(catDto.getCategoryCode())
//                        .categoryName(catDto.getCategoryName())
//                        .build())
//                .orElseThrow(() -> new IllegalArgumentException("Category is required"));

        Category category = categoryRepository.findByCategoryCode(dto.getCategoryCode())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category code: " + dto.getCategoryCode()));

        restaurant.setRestaurantName(dto.getRestaurantName());
        restaurant.setLocation(dto.getLocation());
        restaurant.setDescription(dto.getDescription());
        restaurant.setParking(dto.getParking());
        restaurant.setImageUrl(dto.getImageUrl());
        restaurant.setCategory(category);
        restaurant.setContactNumber(dto.getContactNumber());
        restaurant.setOpeningHours(dto.getOpeningHours());
        restaurant.setAverageRating(dto.getAverageRating());
        restaurant.setDailyLimitedTeams(dto.getDailyLimitedTeams());
        restaurant.setAvailableTeams(dto.getAvailableTeams());

        int reservationCount = reservationRepository.findActiveByRestaurantId(id).size();
        restaurant.setCurrentWaitingTeams(reservationCount);
        restaurant.setPredictedWaitingTime(reservationCount * getRandomMultiplier());

        return toDto(restaurantRepository.save(restaurant));
    }

    private int getRandomMultiplier() {
        int[] multipliers = {3, 4, 5};
        return multipliers[(int) (Math.random() * multipliers.length)];
    }

    @Transactional
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}

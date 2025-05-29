package com.t8.backend.t8.backend.controller;

import com.t8.backend.t8.backend.entity.Member;
import com.t8.backend.t8.backend.entity.Restaurant;
import com.t8.backend.t8.backend.repository.MemberRepository;
import com.t8.backend.t8.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testdata")
@RequiredArgsConstructor
public class TestDataController {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @PostMapping("/init")
    public String initTestData() {
        // Member 데이터 생성
        Member member1 = new Member();
        member1.setName("홍길동");
        member1.setEmail("hong@example.com");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setName("김철수");
        member2.setEmail("kim@example.com");
        memberRepository.save(member2);

        // Restaurant 데이터 생성
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setRestaurantName("맛있는 식당");
        restaurant1.setLocation("서울");
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = new Restaurant();
        restaurant2.setRestaurantName("행복한 밥집");
        restaurant2.setLocation("부산");
        restaurantRepository.save(restaurant2);

        return "Test data initialized: 2 members, 2 restaurants";
    }
}

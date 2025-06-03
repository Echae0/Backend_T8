package com.t8.backend.t8.backend.security.repository;

import com.t8.backend.t8.backend.security.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<com.t8.backend.t8.backend.security.entity.UserInfo, Integer> {
    Optional<com.t8.backend.t8.backend.security.entity.UserInfo> findByEmail(String username);
}
package com.t8.backend.t8.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {
    // 프로덕션 전용 빈 등록
}
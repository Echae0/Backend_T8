// src/main/java/com/t8/backend/t8/backend/config/JpaAuditingConfig.java
package com.t8.backend.t8.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration // 이 클래스가 스프링 설정 클래스임을 명시
@EnableJpaAuditing // JPA Auditing 기능을 여기서 활성화
public class JpaAuditingConfig {
    // 이 클래스 내부에 특별히 추가할 빈이 없다면 비워둬도 됩니다.
}
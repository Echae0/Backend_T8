package com.t8.backend.t8.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = {"com.t8.backend.t8.backend"}) // 또는 더 구체적인 패키지들
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
package com.example.bookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableFeignClients
public class BookServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookServerApplication.class, args);
	}

}

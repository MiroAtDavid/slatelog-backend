package com.slatelog.slatelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@SpringBootApplication()
public class SlatelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlatelogApplication.class, args);
	}

}
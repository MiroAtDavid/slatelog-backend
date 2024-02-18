package com.slatelog.slatelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

public class SlatelogApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlatelogApplication.class, args);
	}

}
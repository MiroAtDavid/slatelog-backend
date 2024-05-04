package com.slatelog.slatelog.faker;

import com.slatelog.slatelog.SlatelogApplication;
import org.springframework.boot.SpringApplication;

public class FakeDataLoaderRunner {
    private static final int FAKED_USERS = 30;
    private static final int FAKED_EVENTS = 10;

    public static void main(String[] args) {
        System.out.println("Loading fake data...");

        // Load the Spring Boot context
        // try with resources -> close the context after the block
        try (var context = SpringApplication.run(SlatelogApplication.class, args)) {
            var faker = context.getBean(FakeDataLoader.class);

            // TODO Challenge: Load it from the command line
            faker.loadFakeData(FAKED_USERS, FAKED_EVENTS);
        }
    }
}

package com.slatelog.slatelog.presentation;

import com.slatelog.slatelog.presentation.commands.Commands.UserRegistrationCommand;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public abstract class ApiHelpers {
    public static final String API_REGISTRATION = "/api/registration";

    public static Response registerUser(UserRegistrationCommand command) {
        // spotless:off
        Response response =
                given()
                        .contentType("application/json")
                        .body(command)
                        .when()
                        .post(API_REGISTRATION)
                        .then()
                        .extract()
                        .response();
        // spotless:on

        return response;
    }
}
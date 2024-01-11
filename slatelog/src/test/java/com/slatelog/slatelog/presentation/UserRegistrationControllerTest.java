package com.slatelog.slatelog.presentation;

import org.junit.jupiter.api.Test;

import static com.slatelog.slatelog.fixture.UserCommandFixture.USER_REGISTRATION_COMMAND;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserRegistrationControllerTest extends AbstractControllerTest {
    @Test
    // givenXXX_whenXXX_ThenXXX
    public void givenNonExistingUser_whenUserRegisters_ThenAccountIsCreated() {
        // When
        var response = ApiHelpers.registerUser(USER_REGISTRATION_COMMAND);

        // Then
        assertThat(response.getStatusCode(), equalTo(201));
        // TODO inspect User Json response with JsonPath
    }
}
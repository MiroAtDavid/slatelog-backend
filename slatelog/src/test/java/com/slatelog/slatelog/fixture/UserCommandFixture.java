package com.slatelog.slatelog.fixture;

import com.slatelog.slatelog.presentation.commands.Commands;

import static com.slatelog.slatelog.fixture.UserFixture.*;

public class UserCommandFixture {
    public static final Commands.UserRegistrationCommand USER_REGISTRATION_COMMAND =
    new Commands.UserRegistrationCommand(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME);
}

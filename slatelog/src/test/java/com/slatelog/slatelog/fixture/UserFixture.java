package com.slatelog.slatelog.fixture;

import com.slatelog.slatelog.domain.user.Profile;
import com.slatelog.slatelog.domain.user.Role;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.security.password.PasswordService;
import com.slatelog.slatelog.security.password.PasswordService.EncodedPassword;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

public class UserFixture {
    public static final String EMAIL = "david@spengergasse.at";
    public static final String PASSWORD = "spengergasse";
    public static final String FIRST_NAME = "Miro";
    public static final String LAST_NAME = "David";

    public static final PasswordService passwordService =
            new PasswordService (createDelegatingPasswordEncoder());

    private static final EncodedPassword encodedPassword = passwordService.encode(PASSWORD);

    public static User createUser() {
        var profile = new Profile(FIRST_NAME, LAST_NAME);
        var user = new User(EMAIL, encodedPassword, Role.USER, profile);
        return user;
    }

}

package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.user.Profile;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.email.EmailService;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.presentation.commands.Commands;
import com.slatelog.slatelog.security.password.PasswordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.slatelog.slatelog.domain.user.Role.USER;


@Service
@RequiredArgsConstructor
public class OAuth2RegistrationService {
    private final Logger LOGGER = LoggerFactory.getLogger(OAuth2RegistrationService.class);

    // Helps us to retrieve users from the database.
    private final UserQueryService userQueryService;

    // Helps us to hash passwords and check password strength.
    private final PasswordService passwordService;

    // Helps us to send emails.
    private final EmailService emailService;

    // Helps us to save users in the database.
    private final UserRepository userRepository;

    // Register User

    public User oauth2Register(String email) {

        LOGGER.info("User registration with email {}", email);

        // 1. Check if email is not taken if not throw exception
        userQueryService.checkEmailNotTaken(email);

        User user = createOAuth2User(email);
        var savedUser = userRepository.save(user);
        LOGGER.info("OAuth2User registration with email {} successful", email);

        return savedUser;

    }

    public void verify(User user) {
        LOGGER.info("User account verification with id {}", user.getId());

        // 1. Retrieve user if not throw exception
        User verifiedUser = userQueryService.findById(user.getId());

        // 2. Verify user if not throw exception
        verifyOAuth2User(verifiedUser);

        // 3. Save user in DB
        userRepository.save(verifiedUser);

        LOGGER.info("User verification with id {} successful", user.getId());
    }

    private User createOAuth2User(String email) {
        Profile profile = new Profile(email, email);
        User user = new User(email, USER, profile);
        return user;
    }

    private void verifyOAuth2User(User user) {
        user.getAccount().setEnabled(true);
    }
}

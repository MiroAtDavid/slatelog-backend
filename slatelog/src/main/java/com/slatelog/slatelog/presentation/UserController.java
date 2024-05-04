package com.slatelog.slatelog.presentation;


import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.security.web.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);

    @GetMapping
    public User login(@AuthenticationPrincipal SecurityUser principal) {
        LOGGER.debug("User contoller#login {}", principal);
            return principal.getUser();
    }
}
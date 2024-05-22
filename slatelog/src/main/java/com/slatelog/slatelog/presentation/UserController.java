package com.slatelog.slatelog.presentation;

import com.slatelog.slatelog.domain.user.Profile;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.presentation.views.Views;
import com.slatelog.slatelog.security.web.SecurityUser;
import com.slatelog.slatelog.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public Views.LoginView login(@AuthenticationPrincipal Object principal) {
        if (principal instanceof SecurityUser securityUser) {
            LOGGER.debug("UserController#login {}", principal);
            return userService.login(securityUser.getUser());
        } else if (principal instanceof OAuth2User oauth2User) {
            //Views.LoginView l = userService.processOAuthPostLogin(oauth2User.getAttribute("email"));
            //Optional<User> user = userRepository.findById(l.user().id());
            Optional<User> u = userRepository.findByEmail(oauth2User.getAttribute("email"));
            //SecurityUser s = new SecurityUser(u.get());
            LOGGER.debug("UserController#login {}", u);
            return userService.processOAuthPostLogin(oauth2User.getAttribute("email"));
        } else {
            throw new IllegalArgumentException("Unknown principal type");
        }
    }

    @GetMapping("/profile")
    public Profile getProfile(@AuthenticationPrincipal SecurityUser principal) {
        return userService.getUserProfile(principal.getUser());
    }


}
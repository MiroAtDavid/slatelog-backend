package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.user.Profile;
import com.slatelog.slatelog.domain.user.Provider;
import com.slatelog.slatelog.domain.user.Role;
import com.slatelog.slatelog.persistance.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.views.LoginViewMapper;
import com.slatelog.slatelog.presentation.views.Views.LoginView;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final LoginViewMapper mapper = LoginViewMapper.INSTANCE;

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final OAuth2RegistrationService oAuth2RegistrationService;

    // private final MessengerRepository messengerRepository;

    public LoginView login(User user) {
        LOGGER.debug("User login {}", user);

        // Fetch all associated data for that user from repositories (e.g. posts, messengers, etc.).
        List<Event> events = eventRepository.findByUserId(user.getId());
        // List<Messenger> messenger = messengerRepository.findByParticipantId(user.getId());

        LoginView loginView = mapper.toLoginView(user, events);
        LOGGER.debug("User login sucessfull {}", loginView);
        return loginView;
    }

    public LoginView processOAuthPostLogin(String username) {
        Optional<User> existUser = userRepository.findByEmail(username);
        if (existUser.isEmpty()) {

            // Register OAuth2User
            User newUser = oAuth2RegistrationService.oauth2Register(username);

            // Fetch all associated data for that user from repositories (e.g. posts, messengers, etc.).
            List<Event> events = eventRepository.findByUserId(newUser.getId());
            // List<Messenger> messenger = messengerRepository.findByParticipantId(user.getId());

            LoginView loginView = mapper.toLoginView(newUser, events);
            LOGGER.debug("OAuth2User login successful {}", loginView);
            return loginView;
        }
        User userExist = existUser.get();
        List<Event> events = eventRepository.findByUserId(userExist.getId());
        LoginView loginViewNew = mapper.toLoginView(userExist, events );
        LOGGER.debug("Existing OAuth2User login successfully {}", loginViewNew);

        return loginViewNew;
    }

    public Profile getUserProfile(User user) {
        return user.getProfile();
    }
}

package com.slatelog.slatelog.service;

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


@Service
@RequiredArgsConstructor
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final LoginViewMapper mapper = LoginViewMapper.INSTANCE;

    private final EventRepository eventRepository;

    // private final MessengerRepository messengerRepository;

    public LoginView login(User user) {
        LOGGER.debug("User login {}", user);

        // Fetch all associated data for that user from repositories (e.g. posts, messengers, etc.).
        List<Event> posts = eventRepository.findByUserId(user.getId());
        // List<Messenger> messenger = messengerRepository.findByParticipantId(user.getId());

        LoginView loginView = mapper.toLoginView(user, posts);
        LOGGER.debug("User login sucessfull {}", loginView);
        return loginView;
    }
}

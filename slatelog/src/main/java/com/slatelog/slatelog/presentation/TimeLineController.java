package com.slatelog.slatelog.presentation;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.presentation.views.Views;
import com.slatelog.slatelog.security.web.SecurityUser;
import com.slatelog.slatelog.service.EventService;
import com.slatelog.slatelog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TimeLineController {
    private final EventService eventService;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/timeline")
    public List<Event> findAllEvents(@AuthenticationPrincipal Object principal) {
        if (principal instanceof SecurityUser securityUser) {
            return eventService.findAllUserEvents(securityUser.getUser().getId());
        } else if (principal instanceof OAuth2User oauth2User) {
            //Views.LoginView l = userService.processOAuthPostLogin(oauth2User.getAttribute("email"));
            //Optional<User> user = userRepository.findById(l.user().id());
            //userRepository.findByEmail(oauth2User.getAttribute("email"));
            Optional<User> u = userRepository.findByEmail(oauth2User.getAttribute("email"));
            User s = u.get();
            return eventService.findAllUserEvents(s.getId());
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("No principal");
            }

            if (authentication instanceof AnonymousAuthenticationToken) {
                System.out.println("Anonymous user");
            }

            Object principalp = authentication.getPrincipal();

            if (principalp instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                System.out.println("User: " + userDetails.getUsername());
            } else {
                System.out.println(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown principal type: " + principal.getClass().getName()));
            }
        }
        return null;
    }



}

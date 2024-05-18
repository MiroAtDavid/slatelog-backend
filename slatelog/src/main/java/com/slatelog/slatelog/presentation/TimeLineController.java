package com.slatelog.slatelog.presentation;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.presentation.commands.Commands;
import com.slatelog.slatelog.security.web.SecurityUser;
import com.slatelog.slatelog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class TimeLineController {
    private final EventService eventService;

    @GetMapping("/timeline")
    public List<Event> findAllEvents(@AuthenticationPrincipal SecurityUser principal) {
        return eventService.findAllUserEvents(principal.getUser().getId());
    }

}

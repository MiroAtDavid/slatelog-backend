package com.slatelog.slatelog.presentation;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.presentation.commands.Commands;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import com.slatelog.slatelog.security.web.SecurityUser;
import com.slatelog.slatelog.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public Event getEvent(@PathVariable String eventId) {
        return eventService.getEventById(eventId);
    }

    @PostMapping
    public void createEvent(@AuthenticationPrincipal SecurityUser principal, @RequestBody CreateEventCommand command) {
        eventService.createEvent(principal.getUser(), command);
    }
}

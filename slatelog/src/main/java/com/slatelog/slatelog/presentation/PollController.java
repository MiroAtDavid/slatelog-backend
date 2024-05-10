package com.slatelog.slatelog.presentation;


import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.presentation.commands.Commands.*;
import com.slatelog.slatelog.security.web.SecurityUser;
import com.slatelog.slatelog.service.EventService;
import com.slatelog.slatelog.service.PollService;
import com.slatelog.slatelog.service.TokenQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class PollController {

    private final PollService pollService;
    private final TokenQueryService tokenQueryService;
    private final EventService eventService;

    @GetMapping("/poll")
    public Event getPollEvent(
            @RequestParam String eventId,
            @RequestParam String emailToken
    ) {
        // EventId and emailToken directly from the URL
        // Perform token validation
        if (tokenQueryService.CheckForValidToken(eventId, emailToken))
            //return ResponseEntity.ok().build();
            return pollService.getVoting(eventId);
        else
            return null;
    }

    @PutMapping("/poll")
    public void updateEventVoting(
            @RequestParam String eventId,
            @RequestParam String emailToken,
            @RequestBody UpdateEventVoting command
    ) {
        // EventId and emailToken directly from the URL
        // Perform token validation
        if (tokenQueryService.CheckForValidToken(eventId, emailToken)) {
            //return ResponseEntity.ok().build();
            pollService.updateEventVoting(eventId, emailToken);
        }
    }

}

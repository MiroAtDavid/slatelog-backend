package com.slatelog.slatelog.presentation.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class Commands {


    // --- Registration ---
    //  whiten input = email.trim().toLowerCase();
    public record UserRegistrationCommand(String email, String password, String firstName, String lastName) {
        public String email() {
            return email.trim().toLowerCase();
        }
    }

    // --- Verification ---
    public record UserVerificationCommand(String userId, String tokenId) {}

    // Received Data from FrontEnd
    public record CreateEventCommand (
            String title,
            String description,
            List<String> pollOptions,
            String locationStreet,
            String locationCity,
            String locationState,
            String locationZipCode,
            String deadlineDate,    // TODO
            String deadlineTime,    // TODO
            Set<String> likes,
            Set<String> hashTags,
            List<String> invitationEmails
    ){}

    // Received Data from FrontEnd
    // Get the votes on a poll
    public record UpdateEventVoting (
            List<HashMap<Instant, String>> votes
    ){}

    public record UpdateEventCommand (
            String title,
            String description,
            List<String> pollOptions,
            String locationStreet,
            String locationCity,
            String locationState,
            String locationZipCode,
            String deadlineDate,    // TODO
            String deadlineTime,    // TODO
            Set<String> likes,
            Set<String> hashTags,
            List<String> invitationEmails
    ){}
        // Getters and setters
    }


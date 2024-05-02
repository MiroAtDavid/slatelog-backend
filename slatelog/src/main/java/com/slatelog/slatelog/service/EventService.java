package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(User user, CreateEventCommand command){
        String title = command.title();
        String address = command.address();
        String deadlineDate = command.deadlineDate();
        String deadlineTime = command.deadlineTime();
        String description = command.description();
        List<String> emails = new ArrayList<>(Arrays.asList(command.emails()));
        List<String> dateTimes = new ArrayList<>(Arrays.asList(command.dateTimes()));

        // Combine deadlineDate and deadlineTime into LocalDateTime
        LocalDateTime eventDateTime = LocalDateTime.parse(deadlineDate + "T" + deadlineTime);

        // Convert LocalDateTime to Instant
        Instant eventInstant = eventDateTime.toInstant(ZoneOffset.UTC);
        VoteOption voteOption;

        // Create poll options (assuming you have a method to parse them)
        HashMap<Instant, List<Answer>> pollOptions = new HashMap<>();

        // Create the Poll object
        Poll poll = new Poll(pollOptions);

        // Create Address object
        Address addressObject = new Address(address, address, address, address);

        // Create Invitation object
        Set<Invitation> invitations = emails.stream()
                .map(Invitation::new)
                .collect(Collectors.toSet());


        // Create the Event
        Event event = new Event(user.getId(), title, description, poll, addressObject, invitations, null);

        // Save Event
        var savedEvent = eventRepository.save(event);
        return savedEvent;
    }



    // Other methods...
}

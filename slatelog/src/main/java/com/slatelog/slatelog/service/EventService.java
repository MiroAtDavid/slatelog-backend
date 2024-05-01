package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(User user, CreateEventCommand command){



        // new Event

        var event = new Event();

        // save Event

        var savedEvent = eventRepository.save(event);
    }
}

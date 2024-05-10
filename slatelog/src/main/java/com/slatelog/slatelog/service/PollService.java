package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.persistance.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollService {

    private final EventRepository eventRepository;

    public Event getVoting(String eventId){
        return eventRepository.getEventById(eventId);
    }

    public void updateEventVoting(String eventId, String emailToken) {
        // TODO
    }
}

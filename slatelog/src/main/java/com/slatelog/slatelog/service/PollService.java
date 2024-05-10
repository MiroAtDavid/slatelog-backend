package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Answer;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.event.VoteOption;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollService {

    private final EventRepository eventRepository;
    private final TokenQueryService tokenQueryService;

    public Event getVoting(String eventId){
        return eventRepository.getEventById(eventId);
    }

    public void updateEventVoting(String eventId, String emailToken, Commands.UpdateEventVoting command) {
        // Make sure event is there
        Event event = eventRepository.getEventById(eventId);

        // Assert there is an associated invitee email with the secToken and retrieve it
        String voterEmail = tokenQueryService.CheckForValidVoterEmail(eventId, emailToken);

        // Let's get the votes from the command DTO
        List<HashMap<Instant, String>> votes = command.votes();

        // TODO check for double email in votes
        // TODO create ics file for voter and send mail
        // Bloody magic adds answers to a simple poll
        for (Instant instant : event.getPoll().getPollOptions().keySet()) {
            for (HashMap<Instant, String> map : votes){
                if (map.containsKey(instant)) {
                    Answer vote = new Answer(voterEmail, Instant.now(), VoteOption.valueOf(map.get(instant)));
                    List<Answer> answers = event.getPoll().getPollOptions().values().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                    answers.add(vote);
                }
            }
        }
        // Finally save event with updated poll
        eventRepository.save(event);
    }
}

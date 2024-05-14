package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.email.EmailEventInvitationService;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EmailEventInvitationService emailEventInvitationService;
    private final EventRepository eventRepository;


    public void createEvent(User user, CreateEventCommand command){

        // Combine deadlineDate and deadlineTime into LocalDateTime
        LocalDateTime eventDateTime = LocalDateTime.parse( command.deadlineDate() + "T" + command.deadlineTime(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // TODO !!!!!!!!!! TIMEZONE insant
        ZoneId viennaZoneId = ZoneId.of("Europe/Vienna");

        // Step 2: Convert LocalDateTime to ZonedDateTime in Vienna time zone
        ZonedDateTime eventDateTimeInVienna = eventDateTime.atZone(viennaZoneId);

        // Step 3: Convert ZonedDateTime to Instant
        Instant eventDeadLineVoting = eventDateTimeInVienna.toInstant();

        // Create poll options
        HashMap<Instant, List<Answer>> pollOptions = new HashMap<>();
        for (String dateTime : command.pollOptions()) {
            // Parse each string to Instant
            Instant instant = Instant.parse(dateTime);
            // Associate an empty list of Answers with the Instant key
            pollOptions.put(instant, new ArrayList<>());
        }
        // Create the Poll object
        Poll poll = new Poll(pollOptions, eventDeadLineVoting);

        // Create Address object
        Address location = new Address(command.locationStreet(), command.locationCity(), command.locationState(), command.locationZipCode());

        // Create Invitation object
        Set<Invitation> invitations = command.invitationEmails().stream()
                .map(email -> new Invitation(email, eventDeadLineVoting))
                .collect(Collectors.toSet());

        // HashTag logic
        List<String> extractedHashtags = extractHashtags(command.description());
        Set<HashTag> hashTags = new HashSet<>(Set.of());
        // Output the extracted hashtags
        for (String hashtag : extractedHashtags) {
            hashTags.add(new HashTag(hashtag));
        }

        // Create the Event
        Event event = new Event(user.getId(), command.title(), command.description(), poll, location, invitations, null, hashTags);

        // Send invitation Emails to invitees
        invitations.forEach(invitation -> emailEventInvitationService.sendEventInvitationEmail(event, invitation));

        // Save Event
        saveEvent(event);
    }

    // Encapsulated Method to save the event
    private void saveEvent(Event event) {
        eventRepository.save(event);
    }

    // Method to retrieve an event by its ID
    public Event getEventById(String eventId) {
        return eventRepository.getEventById(eventId);
    }

    public Event getEventByIdAndUser(User user, String eventId){
        Event event = eventRepository.getEventById(eventId);
        if (event.getUserId().equals(user.getId()))
            return event;
        return null;
    }

    public List<Event> findAllUserEvents(String userId){
        return eventRepository.findByUserId(userId);
    }

    // Helper Methods --------------------------------------------------------------------------------------------------

    // We are extracting HashTags from the event description
    private static List<String> extractHashtags(String text) {
        List<String> hashtags = new ArrayList<>();

        // Define the regular expression pattern to match hashtags
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(text);

        // Find all occurrences of hashtags in the text
        while (matcher.find()) {
            // Add the matched hashtag (group 1) to the list
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }
}

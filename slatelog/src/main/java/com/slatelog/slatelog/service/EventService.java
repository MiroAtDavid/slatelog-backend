package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public void createEvent(User user, CreateEventCommand command){
        String title = command.title();
        String state = command.locationState();
        String city = command.locationCity();
        String street = command.locationStreet();
        String zipcode = command.locationZipCode();
        String deadlineDate = command.deadlineDate();
        String deadlineTime = command.deadlineTime();
        String description = command.description();
        List<String> emails = command.invitationEmails();
        List<String> dateTimes = command.pollOptions();

        // Combine deadlineDate and deadlineTime into LocalDateTime
        LocalDateTime eventDateTime = LocalDateTime.parse(deadlineDate + "T" + deadlineTime);

        // TODO - implementation - Convert LocalDateTime to Instant
        Instant eventInstant = eventDateTime.toInstant(ZoneOffset.UTC);
        VoteOption voteOption;
        // Create poll options
        HashMap<Instant, List<Answer>> pollOptions = new HashMap<>();
        for (String dateTime : dateTimes) {
            // Parse each string to Instant
            Instant instant = Instant.parse(dateTime);

            // Associate an empty list of Answers with the Instant key
            pollOptions.put(instant, new ArrayList<>());
        }
        // Create the Poll object
        Poll poll = new Poll(pollOptions);

        // Create Address object
        Address location = new Address(street, city, state, zipcode);

        // Create Invitation object
        Set<Invitation> invitations = emails.stream()
                .map(Invitation::new)
                .collect(Collectors.toSet());

        Set<HashTag> hashTags = Set.of();

        // Create the Event
        Event event = new Event(user.getId(), title, description, poll, location, invitations, null, hashTags);
        // event.setIcsFileData(createIcsFileData(event)); //TODO check what's the smarter way to implement
        // Save Event
        saveEvent(event);
    }

    // Encapsulated Method to save the event
    private void saveEvent(Event event) {
        eventRepository.save(event);
    }

    // Create Icalendar (*.ics) data
    private byte[] createIcsFileData(Event event) {

        Set<Instant> eventDates = event.getPoll().getPollOptions().keySet();
        List<Instant> instantList = new ArrayList<>(eventDates);

        LocalDateTime eventCreatedAt = LocalDateTime.now();
        LocalDateTime eventEndTime = eventCreatedAt.plusHours(1);
        String eventLocationState = event.getLocation().state();
        String eventLocationCity = event.getLocation().city();
        String eventLocationZip = event.getLocation().zipCode();
        String eventLocationStreet = event.getLocation().street();
        String eventLocation = eventLocationState + ", " + eventLocationCity + " " + eventLocationZip + ", " + eventLocationStreet;
        String [] invitationEmails = event.getInvitations().stream()
                .map(Invitation::getEmail)
                .toArray(String[]::new);

        // Format date-time according to iCalendar format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        // Construct iCalendar data
        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n");
        icsContent.append("VERSION:2.0\n");

        // Iterate over each Instant in the list
        for (Instant startTime : instantList) {
            // Calculate end time by adding one hour
            Instant endTime = startTime.plus(Duration.ofHours(1));

            // Convert Instant to ZonedDateTime with UTC time zone
            ZonedDateTime startDateTime = ZonedDateTime.ofInstant(startTime, ZoneId.of("UTC"));
            ZonedDateTime endDateTime = ZonedDateTime.ofInstant(endTime, ZoneId.of("UTC"));

            // Format start and end times
            String formattedStart = startDateTime.format(formatter);
            String formattedEnd = endDateTime.format(formatter);

            // Append event details to the icsContent StringBuilder
            icsContent.append("BEGIN:VEVENT\n");
            icsContent.append("DTSTART:").append(formattedStart).append("\n");
            icsContent.append("DTEND:").append(formattedEnd).append("\n");
            icsContent.append("SUMMARY:").append(event.getTitle()).append("\n");
            icsContent.append("DESCRIPTION:").append(event.getDescription()).append("\n");
            icsContent.append("LOCATION:").append(eventLocation).append("\n");
            icsContent.append("ORGANIZER:").append(event.getUserId()).append("\n");
            for (String attendee : invitationEmails) {
                icsContent.append("ATTENDEE:").append(attendee).append("\n");
            }
            icsContent.append("END:VEVENT\n");
        }
        byte[] ics = icsContent.toString().getBytes();
        return ics;
    }
}

package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.presentation.commands.Commands.CreateEventCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Instant eventDeadLineVoting = eventDateTime.toInstant(ZoneOffset.UTC);
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
        Poll poll = new Poll(pollOptions, eventDeadLineVoting);

        // Create Address object
        Address location = new Address(street, city, state, zipcode);

        // Create Invitation object
        Set<Invitation> invitations = emails.stream()
                .map(email -> new Invitation(email, eventDeadLineVoting))
                .collect(Collectors.toSet());


        // HashTag logic
        List<String> extractedHashtags = extractHashtags(description);
        Set<HashTag> hashTags = new HashSet<>(Set.of());

        // Output the extracted hashtags
        for (String hashtag : extractedHashtags) {
            hashTags.add(new HashTag("#" + hashtag));
        }

        // Avoiding empty HashTag set, whilst showing it is possible
        //if (hashTags.isEmpty()) {
        //    hashTags.add(new HashTag("#" + title));
        //}

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

    // Method to retrieve an event by its ID
    public Event getEventById(String eventId) {
        return eventRepository.getEventById(eventId);
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    // Helper Methods --------------------------------------------------------------------------------------------------

    // We are extracting HashTags from the event description
    public static List<String> extractHashtags(String text) {
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

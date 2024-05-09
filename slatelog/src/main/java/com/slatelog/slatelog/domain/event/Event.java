package com.slatelog.slatelog.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.slatelog.slatelog.domain.BaseEntity;
import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.media.Media;
import com.slatelog.slatelog.service.IcsCalendarService;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.slatelog.slatelog.foundation.AssertUtil.*;
import static com.slatelog.slatelog.foundation.EntityUtil.generateUUIDv4;

/**
 * Represents an event with associated details such as title, description, poll, and location.
 */
@Getter
@Setter
@Document("event")
@TypeAlias("event")
public class Event extends BaseEntity<String> {

    @Indexed()
    private String userId;
    private String title;
    private @Nullable String description;
    private Poll poll;
    private @Nullable Address location;
    private @Nullable Set<Invitation> invitations;
    private @Nullable List<Media> medias;
    private @Nullable Set<HashTag> hashTags;
    private Set<Like> likes;
    private Instant votingDeadLine;
    private byte[] icsFileData;

    /**
     * Default constructor for Spring Data.
     *
     * @param id The unique identifier for the Event.
     */
    @PersistenceCreator
    @JsonCreator
    protected Event(String id) {
        super(id);
    }

    /**
     * Constructor for creating a new Event.
     * @param userId      The user ID to whom the Event belongs.
     * @param title       The title of the Event.
     * @param description The optional description of the Event.
     * @param poll        The poll associated with the Event.
     * @param location    The optional location of the Event.
     * @param invitations  Invitations for the Event.
     * @param medias      Optional media associated with the Event.
     */
    public Event (String userId, String title, @Nullable String description, Poll poll, @Nullable Address location, @Nullable Set<Invitation> invitations, @Nullable List<Media> medias, @Nullable Set<HashTag> hashTags) {
        super(generateUUIDv4());

        isTrue(title != null || medias != null, "text or medias must not be null");
        this.userId = isNotNull(userId, "userId");
        this.title = isNotNull(title, "title");
        this.description = hasMaxTextOrNull(description, 4096, "desctiption");
        this.poll = isNotNull(poll, "poll");
        this.location = location;
        this.medias = hasMaxSizeOrNull(medias, 10, "medias");
        this.hashTags = hashTags;
        this.likes = new HashSet<>();
        this.invitations = invitations;
        this.icsFileData = createIcsFileData();
    }



    private byte[] createIcsFileData() {


        // TODO this is just a test needs rethinking and refactoring
        //  we need to retrieve the invitation email on voting
        //  this needs however to be done with the security token
        //  there for we need a GET mapping for the secToken Link
        //  however this is a bread crumb on how to retrieve deep data
        //  however we are just calling it here so it gets executed on run
        testCheckForInvitationEmailOnVoting();
        // TODO /end

        Set<Instant> eventDates = getPoll().getPollOptions().keySet();
        List<Instant> instantList = new ArrayList<>();
        instantList.addAll(eventDates);

        LocalDateTime eventCreatedAt = LocalDateTime.now();
        LocalDateTime eventEndTime = eventCreatedAt.plusHours(1);
        String eventLocationState = getLocation().state().toString();
        String eventLocationCity = getLocation().city().toString();
        String eventLocationZip = getLocation().zipCode().toString();
        String eventLocationStreet = getLocation().street() .toString();
        String eventLocation = eventLocationState + ", " + eventLocationCity + " " + eventLocationZip + ", " + eventLocationStreet;

        String [] invitationEmails = getInvitations().stream()
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
            icsContent.append("DTSTART:" + formattedStart + "\n");
            icsContent.append("DTEND:" + formattedEnd + "\n");
            icsContent.append("SUMMARY:" + title + "\n");
            icsContent.append("DESCRIPTION:" + description + "\n");
            icsContent.append("LOCATION:" + eventLocation + "\n");
            icsContent.append("ORGANIZER:" + getUserId() + "\n");
            for (String attendee : invitationEmails) {
                icsContent.append("ATTENDEE:" + attendee + "\n");
            }
            icsContent.append("END:VEVENT\n");
        }
        byte[] ics = icsContent.toString().getBytes();
        return ics;
    }


    // TODO
    private void testCheckForInvitationEmailOnVoting() {
        //System.out.println(getPoll().getPollOptions().values().stream().toString());
        Collection<List<Answer>> answers = getPoll().getPollOptions().values();
        assert getInvitations() != null;
        List<Invitation> invitationList = getInvitations().stream().toList();
        // Iterate over the outer collection
        for (List<Answer> answerList : answers) {
            // Iterate over each inner list
            for (Answer answer : answerList) {
                // Access individual Answer objects
                // You can do something with 'answer' here
                // System.out.println(answer.getVoterEmail());
                for (Invitation inv : invitationList) {
                    if (inv.getEmail().equals(answer.getVoterEmail())) {
                        System.out.println(inv.getEmail() + " Match");
                    }
                }
            }
        }
    }



}



package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.email.EmailPollClosedService;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.presentation.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PollService {

    private final EventRepository eventRepository;
    private final TokenQueryService tokenQueryService;
    private final EmailPollClosedService emailPollClosedService;
    private final UserRepository userRepository;

    public Event getVoting(String eventId){
        return eventRepository.getEventById(eventId);
    }

    public void updateEventVoting(String eventId, String emailToken, Commands.UpdateEventVoting command) {
        // Fetch the event
        Event event = eventRepository.getEventById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event not found for ID: " + eventId);
        }
        // Validate voter email
        String voterEmail = tokenQueryService.checkForValidVoterEmail(eventId, emailToken);
        if (voterEmail == null) {
            throw new IllegalArgumentException("Invalid email token: " + emailToken);
        }
        // Get votes from the command DTO
        List<HashMap<Instant, String>> votes = command.votes();
        List<Instant> positiveVotes = new ArrayList<>();
        VoteOption yes = VoteOption.Yes;

        // Process votes and update poll
        Poll poll = event.getPoll();
        for (Instant instant : poll.getPollOptions().keySet()) {
            for (HashMap<Instant, String> map : votes) {
                if (map.containsKey(instant)) {
                    Answer vote = new Answer(voterEmail, Instant.now(), VoteOption.valueOf(map.get(instant)));
                    if (vote.getVoteOption().equals(yes)) {
                        positiveVotes.add(instant);
                    }
                    poll.getPollOptions().get(instant).removeIf(v -> v.getVoterEmail().equals(voterEmail));
                    poll.getPollOptions().get(instant).add(vote);
                }
            }
        }
        // Create and set ICS file data for the invitation
        Invitation invitation = tokenQueryService.checkForInvitation(eventId, emailToken);
        if (invitation == null) {
            throw new IllegalArgumentException("No invitation found for eventId: " + eventId + " and emailToken: " + emailToken);
        }
        invitation.setIcsFileDataInvitee(createInviteeIcsFileData(event, positiveVotes));

        // Update invitations and save event
        Set<Invitation> invitations = event.getInvitations();
        invitations.removeIf(inv -> inv.getEmail().equals(invitation.getEmail())); // Remove old invitation
        invitations.add(invitation); // Add updated invitation
        event.setInvitations(invitations); // Ensure the list is set back to the event

        // Save the event
        eventRepository.save(event);
    }


    // Helper to create the ics file data for the invitee
    private byte[] createInviteeIcsFileData(Event event, List<Instant> eventTimes) {
        List<Instant> instantList = new ArrayList<>();
        instantList.addAll(eventTimes);

        LocalDateTime eventCreatedAt = LocalDateTime.parse(event.getCreatedAt().toString().substring(0, event.getCreatedAt().toString().length() - 1));
        LocalDateTime eventEndTime = eventCreatedAt.plusHours(1);
        String eventLocationState = event.getLocation().state().toString();
        String eventLocationCity = event.getLocation().city().toString();
        String eventLocationZip = event.getLocation().zipCode().toString();
        String eventLocationStreet = event.getLocation().street() .toString();
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
            // TODO duration of the event
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
            icsContent.append("SUMMARY:" + event.getTitle() + "\n");
            icsContent.append("DESCRIPTION:" + event.getDescription() + "\n");
            icsContent.append("LOCATION:" + eventLocation + "\n");
            User creatorUser = userRepository.findUserById(event.getUserId());
            String creatorEmail = creatorUser.getEmail();
            icsContent.append("ORGANIZER:" + creatorEmail + "\n");
            for (String attendee : invitationEmails) {
                icsContent.append("ATTENDEE:" + attendee + "\n");
            }
            icsContent.append("END:VEVENT\n");
        }
        byte[] ics = icsContent.toString().getBytes();
        return ics;
    }


    // the event needs a boolean value isNotified
    // check out @CompoundIndex first value should  be isNotified second index isClosed
    // "0 0 * ? * *" = hourly
    @Scheduled(cron = "0 */1 * ? * *")
    public void closeVoting() throws InterruptedException {
        for (Event event : eventRepository.findEventsByMailSentFalse()) {
            if (!event.isPollOpen()) {
                if (event.getPoll().getPollCloseDate().isBefore(Instant.now())){
                    event.setPollOpen(false);
                    eventRepository.save(event);
                }
                emailPollClosedService.sendPollClosedEmail(event);
                event.setMailSent(true);
                eventRepository.save(event);
            } else {
                if (event.getPoll().getPollCloseDate().isBefore(Instant.now())){
                    event.setPollOpen(false);
                    eventRepository.save(event);
                }
            }
        }
    }

   // @Scheduled(cron = "0 */1 * ? * *")
   // public void closeVoting3() throws InterruptedException {
   //     eventRepository.findByPollPollCloseDateAfterAndMailSent(Instant.now(), false)
   //             .forEach(event -> {
   //                     emailPollClosedService.sendPollClosedEmail(event);
   //                     event.setMailSent(true);
   //                     eventRepository.save(event);
   //             });
   // }
}

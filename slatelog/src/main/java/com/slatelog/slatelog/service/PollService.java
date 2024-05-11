package com.slatelog.slatelog.service;

import com.slatelog.slatelog.domain.event.Answer;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.domain.event.VoteOption;
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
import java.util.stream.Collectors;

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
        // Make sure event is there
        Event event = eventRepository.getEventById(eventId);

        // Assert there is an associated invitee email with the secToken and retrieve it
        String voterEmail = tokenQueryService.checkForValidVoterEmail(eventId, emailToken);

        // Let's get the votes from the command DTO
        List<HashMap<Instant, String>> votes = command.votes();

        // Create a list which will contain the positive votes for the ics file
        List<Instant> positiveVotes = new ArrayList<>();
        VoteOption yes = VoteOption.Yes;

        // Bloody magic adds answers to a simple poll
        for (Instant instant : event.getPoll().getPollOptions().keySet()) {
            for (HashMap<Instant, String> map : votes){
                if (map.containsKey(instant)) {
                    Answer vote = new Answer(voterEmail, Instant.now(), VoteOption.valueOf(map.get(instant)));
                    // If vote is positive let's get this done
                    if (vote.getVoteOption().equals(yes))
                        positiveVotes.add(instant);
                    // Continue on with service logic
                    List<Answer> answers = event.getPoll().getPollOptions().values().stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());
                    answers.removeIf(answer -> answer.getVoterEmail().equals(voterEmail));
                    answers.add(vote);
                }
            }
        }

        // Create ics file for voter and send mail
        Invitation invitation = tokenQueryService.checkForInvitation(eventId, emailToken);
        invitation.setIcsFileDataInvitee(createInviteeIcsFileData(event, positiveVotes));
        // TODO send mail

        // Finally save event with updated poll
        eventRepository.save(event);
    }

    // TODO see above - send mail to invitee that has voted
    // Helper to create the ics file data for the invitee, however maybe refactoring to place both ics_s at a better place
    // One is directly in the event domain class for the event creator and for now, this one remains here
    private byte[] createInviteeIcsFileData(Event event, List<Instant> eventTimes) {
        List<Instant> instantList = new ArrayList<>();
        instantList.addAll(eventTimes);

        LocalDateTime eventCreatedAt = LocalDateTime.parse(event.getCreatedAt().toString());
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
            icsContent.append("ORGANIZER:" + event.getUserId() + "\n");
            for (String attendee : invitationEmails) {
                icsContent.append("ATTENDEE:" + attendee + "\n");
            }
            icsContent.append("END:VEVENT\n");
        }
        byte[] ics = icsContent.toString().getBytes();
        return ics;
    }

    // TODO setting up event closed notification
    // the event needs a boolean value isNotified
    // check out @CompundIndex first value should  be isNotified second index isClosed
    // "0 0 * ? * *" = hourly
    @Scheduled(cron = "0 */1 * ? * *")
    public void closeVoting() throws InterruptedException {
        for (Event event : eventRepository.findByMailSent(false)) {
            if (event.getPoll().isPollOpen()) {
                // TODO here we need to send the mail
                 emailPollClosedService.sendPollClosedEmail(event);
                // TODO once the mail is sent event.isMailSent has to be set to (true)
                // TODO emails go out, however the logic is not correct !!!!!!! urgent review
                System.out.println(Instant.now().toString());
                System.out.println("Poll closed at: ");
                System.out.println(event.getPoll().getPollCloseDate().toString());
                event.setMailSent(true);
            } else {
                System.out.println("Poll is open, closes at:");
                System.out.println(event.getPoll().getPollCloseDate().toString());
            }
        }
    }
}

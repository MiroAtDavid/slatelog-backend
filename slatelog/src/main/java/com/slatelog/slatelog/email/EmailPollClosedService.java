package com.slatelog.slatelog.email;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailPollClosedService {

    // Mail sender for sending invitation emails
    private final EMailSender mailSender;
    private final UserQueryService userQueryService;

    // Protocol of the server where the application is running
    @Value("http")
    private String protocol;

    // Domain name of the server where the application is running
    @Value("localhost")
    private String domain;

    // Port of the server where the application is running
    @Value("8080")
    private String port;

    // The event poll link template used to generate invitation links
    private final String EVENT_LINK = "%s://%s:%s/api/event/eventId=%s";

    // Send Invitation Email
    // Sends an invitation email to the recipient for the given event and invitation
    public void sendPollClosedEmail(Event event) {
        String userId = event.getUserId();
        User user = userQueryService.findById(userId);
        // Extracting receiver email from the invitation
        var receiver = user.getEmail();
        // Generating subject for the invitation email
        var subject = getPollClosedEmailSubject();
        // Generating body content for the invitation email
        var body = getPollClosedEmailBody(event, user);
        // Sending the email using the mail sender
        mailSender.sendMail(new EmailDTO(receiver, subject, body));
    }

    // Generates the subject for the invitation email
    public String getPollClosedEmailSubject() {
        return "Your event poll has been closed, you can now schedule the event";
    }

    // Generates the body content for the invitation email
    public String getPollClosedEmailBody(Event event, User user) {
        // Generating the complete invitation link using the event ID and email token
        String token = user.getAccount().getVerificationEmailToken();
        // TODO why token?????????
        return String.format(EVENT_LINK, protocol, domain, port, event.getId(),token);
    }

}

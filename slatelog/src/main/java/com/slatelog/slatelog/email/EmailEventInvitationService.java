package com.slatelog.slatelog.email;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.security.token.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class EmailEventInvitationService {

    // Mail sender for sending invitation emails
    private final EMailSender mailSender;

    // Protocol of the server where the application is running
    @Value("http")
    private String protocol;


    // POP3-Server:
    // SMTP-Server:
    // Benutzername:
    // Domain name of the server where the application is running

    @Value("localhost")
    private String domain;

    // Port of the server where the application is running
    @Value("4200")
    private String port;

    // The event poll link template used to generate invitation links
    private final String EVENT_LINK = "%s://%s:%s/voting/token?eventId=%s&tokenId=%s";
    // Send Invitation Email
    // Sends an invitation email to the recipient for the given event and invitation
    public void sendEventInvitationEmail(Event event, Invitation invitation) {
        // Extracting receiver email from the invitation
        var receiver = invitation.getEmail();
        // Generating subject for the invitation email
        var subject = getInvitationEmailSubject();
        // Generating body content for the invitation email
        var body = getInvitationEmailBody(event, invitation);
        // Sending the email using the mail sender
        mailSender.sendMail(new EmailDTO(receiver, subject, body));
    }

    // Generates the subject for the invitation email
    public String getInvitationEmailSubject() {
        return "You have been invited to an Event";
    }

    // Generates the body content for the invitation email
    public String getInvitationEmailBody(Event event, Invitation invitation) {
        // Extracting the email token from the invitation
        String token = invitation.getInvitationToken().getEncodedValue();
        // Generating the complete invitation link using the event ID and email token
        return String.format(EVENT_LINK, protocol, domain, port, event.getId(), token);
    }
}
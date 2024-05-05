package com.slatelog.slatelog.email;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventInvitationService {

    private final EMailSender mailSender;

    // The protocol of the server where the application is running.
    @Value("http")
    private String protocol;

    // The domain name of the server where the application is running.
    @Value("localhost")
    private String domain;

    // The port of the server where the application is running.
    @Value("8080")
    private String port;

    // The event poll link that is sent to the user to vote on event times.
    private final String EVENT_LINK = "%s://%s:%s/api/event/poll?eventId=%s&emailToken=%s";

    // Send Invitation Email
    public void sendInvitationEmail(Event event, Invitation invitation) {
        var receiver = invitation.getEmail();
        var subject = getInvitationEmailSubject();
        var body = getInvitationEmailBody(event, invitation);
        mailSender.sendMail(new EmailDTO(receiver, subject, body));
    }

    public String getInvitationEmailSubject() {
        return "You have been invited to an Event";
    }

    public String getInvitationEmailBody(Event event, Invitation invitation) {
        String token =  invitation.getEmailToken().toString();
        return String.format(EVENT_LINK, protocol, domain, port, event.getId(), token);
    }
}

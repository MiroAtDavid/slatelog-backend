package com.slatelog.slatelog.email;

import com.slatelog.slatelog.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailEventInvitationService {

    private final EMailSender mailSender;

    // private final JavaMailSenderImpl mailSender;
    // private final LoggerMailSenderImpl mailSender;

    // The protocol of the server where the application is running.
    @Value("http")
    private String protocol;

    // The domain name of the server where the application is running.
    @Value("localhost")
    private String domain;

    // The port of the server where the application is running.
    @Value("8080")
    private String port;

    // The verification link that is sent to the user which he has to click to verify his email.
    // TODO!!!!! Create appropriate link for the event poll
    private final String EVENT_LINK = "%s://%s:%s/api/registration/token?userId=%s&tokenId=%s";

    // Send Invitation Email
    // --------------------------------------------------------------------------------------------
    // @Async
    // public void sendVerificationEmail(User user, String tokenId) {
    public void sendVerificationEmail(User user) {
        // We build the EmailDTO object and send it to the mailSender.
        // TODO!!!!! email must be set form the event invitation email list
        var receiver = user.getAccount().getVerificationEmail();
        var subject = getInvitationEmailSubject();
        var body = getInvitationEmailBody(user);
        mailSender.sendMail(new EmailDTO(receiver, subject, body));
    }

    public String getInvitationEmailSubject() {
        return "You have been invited to an Event";
    }

    // TODO!!!!! adjust body with data
    public String getInvitationEmailBody(User user) {
        String token = user.getAccount().getVerificationEmailToken();
        return String.format(EVENT_LINK, protocol, domain, port, user.getId(), token);
    }
}

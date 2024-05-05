package com.slatelog.slatelog.email;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Class Purpose?
// --------------------------------------------------------------------------------------------
// `LoggerEMailSenderImpl` is only created if the `JavaMailSender` is _NOT_ present
// in the spring context.
// `JavaMailSender` is only present if it's properties are set in `application.properties`.
// We create this class to avoid having to configure an SMTP server for development.
// See also: JavaEMailSenderImpl.java

// Annotations used?
// --------------------------------------------------------------------------------------------
// @Service to mark this class as a Spring service
// @ConditionalOnMissingBean(...) to only create this bean if `JavaMailSender` is not present
// @RequiredArgsConstructor Lombok annotation that generates a constructor with all required fields.

@Service
@ConditionalOnMissingBean(JavaMailSender.class) // Conditionally creates an instance if JavaMailSender is missing.
public class LoggerEMailSenderImpl implements EMailSender {

    // Logger for logging email details.
    private final Logger LOGGER = LoggerFactory.getLogger(LoggerEMailSenderImpl.class);

    // Log message template for logging email recipient.
    public static final String LOG_EMAIL_INFO = "Logging email with LoggerMailSenderImpl to RECIPIENT: {}";

    // Log message template for logging email details.
    public static final String LOG_EMAIL_BODY = "RECIPIENT: {} SUBJECT: {} BODY: {}";

    /**
     * Logs the email details including recipient, subject, and body.
     *
     * @param emailDTO The EmailDTO containing the details of the email.
     */
    @Override
    public void sendMail(EmailDTO emailDTO) {
        // Log the email recipient.
        // LOGGER.info(LOG_EMAIL_INFO, emailDTO.recipient());

        // Log the email recipient, subject, and body.
        LOGGER.info(LOG_EMAIL_BODY, emailDTO.recipient(), emailDTO.subject(), emailDTO.body());
    }
}

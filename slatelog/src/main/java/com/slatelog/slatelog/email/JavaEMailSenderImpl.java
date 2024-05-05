package com.slatelog.slatelog.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Class Purpose?
// --------------------------------------------------------------------------------------------
// `JavaEMailSenderImpl` is only created if the `JavaMailSender` _IS_ present in the spring context.
// `JavaMailSender` is only present if it's properties are set in `application.properties`.
// We create this class to send real emails to an SMTP server.
// See also: LoggerEMailSenderImpl.java

// Annotations used?
// --------------------------------------------------------------------------------------------
// @Service to mark this class as a Spring service
// @ConditionalOnBean(...) to only create this bean if `JavaMailSender` is present
// @RequiredArgsConstructor Lombok annotation that generates a constructor with all required fields.


@Service
@ConditionalOnBean(JavaMailSender.class) // Conditionally creates an instance if JavaMailSender is present.
@RequiredArgsConstructor // Lombok annotation for generating constructor with required fields.
public class JavaEMailSenderImpl implements EMailSender {

    // Log message template for email sending.
    public static final String LOG_EMAIL = "Sending email with JavaEMailSenderImpl to \nRECIPIENT: {}";

    // Logger for logging email sending information.
    private final Logger LOGGER = LoggerFactory.getLogger(JavaEMailSenderImpl.class);

    // Suppressing IntelliJ inspection warning for autowiring; this is a false positive.
    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaInjectionPointsAutowiringInspection"})
    private final JavaMailSender mailSender; // Instance of JavaMailSender for sending emails.

    /**
     * Sends an email using the provided EmailDTO.
     *
     * @param emailDTO The EmailDTO containing the necessary information for sending the email.
     */
    @Override
    public void sendMail(EmailDTO emailDTO) {
        try {
            // Log the email recipient.
            LOGGER.info(LOG_EMAIL, emailDTO.recipient());

            // Create a SimpleMailMessage and set its properties.
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailDTO.recipient());
            message.setSubject(emailDTO.subject());
            message.setText(emailDTO.body());

            // Send the email using the JavaMailSender instance.
            mailSender.send(message);
        } catch (MailException e) {
            // If an exception occurs while sending email, wrap it in a RuntimeException and throw.
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

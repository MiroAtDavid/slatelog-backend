package com.slatelog.slatelog.email;

/**
 * Interface for sending emails.
 */
public interface EMailSender {

    /**
     * Sends an email using the provided EmailDTO.
     *
     * @param emailDTO The EmailDTO containing the necessary information for sending the email.
     */
    void sendMail(EmailDTO emailDTO);
}

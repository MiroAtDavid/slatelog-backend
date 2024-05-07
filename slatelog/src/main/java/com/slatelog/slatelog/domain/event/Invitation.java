package com.slatelog.slatelog.domain.event;

import com.slatelog.slatelog.security.token.InvitationEmailToken;
import com.slatelog.slatelog.security.token.Token;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Duration;
import java.time.Instant;

// Class representing an invitation to an event
@Getter
@Setter
public class Invitation {

    // Email address associated with the invitation
    private String email;

    // Token associated with the email for verification
    private @Nullable Token emailToken;

    // Event invitation voting end date
    private Instant tokenExpirationDate;

    // TODO We need to get this from the poll
    private boolean pollIsOpen;

    // Constructor to create an Invitation with an email and token
    public Invitation(String email) {
        this.email = email;
        // TODO implement event poll voting endTime
        setEmailToken(email);
    }

    // Default constructor (
    protected Invitation(){};

    private void setEmailToken(String email){
        // TODO implement event poll voting endTime
        // TODO implement event poll boolean isOpen()
        if (!pollIsOpen){
            emailToken = null;
        } else {
            this.emailToken = InvitationEmailToken.generateInvitationToken(email, Instant.now().plus(Duration.ofHours(24)));
        }

    }



}

package com.slatelog.slatelog.domain.event;

import com.slatelog.slatelog.security.token.InvitationEmailToken;
import com.slatelog.slatelog.security.token.Token;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static com.slatelog.slatelog.foundation.AssertUtil.isNotNull;

// Class representing an invitation to an event
@Getter
@Setter
public class Invitation {

    // Email address associated with the invitation
    private String email;
    // Token associated with the email for verification
    private @Nullable Token invitationToken;
    // Event invitation voting end date
    private @Nullable Instant tokenExpirationDate;
    // TODO !!! We need to get ics file data from the poll for the invitation
    private byte[] icsFileDataInvitee;

    // Constructor to create an Invitation with an email and token
    public Invitation(String email, @Nullable Instant tokenExpirationDate) {
        this.email = isNotNull(email, "email");
        this.tokenExpirationDate = tokenExpirationDate;
        generateInvitationToken(email, tokenExpirationDate);
        this.icsFileDataInvitee = generateIcsVotedFile();
    }

    // Default constructor (
    protected Invitation(){}

    // Generating secToken for the invitations
    private void generateInvitationToken(String email, Instant tokenExpirationDate) {
        if (tokenExpirationDate == null || tokenExpirationDate.isBefore(Instant.now())) {
            invitationToken = null;
        } else {
            Duration durationUntilExpiration = Duration.between(Instant.now(), tokenExpirationDate);
            this.invitationToken = InvitationEmailToken.generateInvitationToken(
                    email,
                    Instant.now().plus(durationUntilExpiration)
            );
        }
    }

    public byte[] generateIcsVotedFile(){
        return new byte[]{};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation invitation = (Invitation) o;
        return Objects.equals(email, invitation.email );
    }

    public int hashCode() {
        return Objects.hash(email);
    }


}

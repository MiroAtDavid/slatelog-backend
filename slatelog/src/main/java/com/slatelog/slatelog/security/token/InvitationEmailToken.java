package com.slatelog.slatelog.security.token;

import com.slatelog.slatelog.domain.event.Invitation;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;

import java.time.Instant;

import static com.slatelog.slatelog.security.token.TokenUtil.generateToken;
@SuperBuilder
@Getter
public class InvitationEmailToken extends Token{
    private final String invitationEmail;

    // TODO Strings are bad for security. Will be explain later.
    @Transient
    private String invitationEmailToken;

    @PersistenceCreator
    public InvitationEmailToken(
            String invitationEmail, String encodedValue, Instant createdAt, Instant expiresAt) {
        super(encodedValue, createdAt, expiresAt);
        this.invitationEmail = invitationEmail;
    }

    // Static factory method
    public static InvitationEmailToken generateInvitationToken(String email, Instant expiresAt) {
        // Generate a secure 128-bit token as UUIDv4 and hash it with Keccak-256.
        var tokenValues = generateToken();

        // Build the token object with the builder design pattern
        return InvitationEmailToken.builder()
                .encodedValue(tokenValues.hashedTokenIdBase64())
                .invitationEmail(email)
                .invitationEmailToken(tokenValues.tokenId())
                .createdAt(Instant.now())
                .expiresAt(expiresAt)
                .build();
    }
}

package com.slatelog.slatelog.domain.user;

import com.slatelog.slatelog.security.token.VerificationEmailToken;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

import static com.slatelog.slatelog.foundation.AssertUtil.isNotNull;
import static com.slatelog.slatelog.security.token.TokenUtil.verifyToken;
import static com.slatelog.slatelog.security.token.VerificationEmailToken.generateEmailToken;
import static java.time.Instant.now;

// How to verify an email address using a One Time Token
// ------------------------------------------------------------
// 1. Create One Time Token
// 2. Hash One Time Token with Keccak-256 and store the hashed value into the DB
// 3. Cleartext One Time Token is sent to the user via email
// /api/registration/token?userId=123&tokenId=456
// 4. User clicks on the link in the email and the One Time Token is sent to the server
// 5. The server verifies the One Time Token by hashing it against the hashed version in the DB
// 6. If the hashes match, and it is not expired then the email is verified

/**
 * Account of a user.
 *
 * <p>Here we can place account information of a user. <br>
 * If account is enabled, the user can log in. <br>
 * If account is disabled, the user cannot log in. <br>
 *
 * <p>A user must verify their email address before they can log in.
 */

// This class in inlined in User.
@Getter
public class Account {
    @Setter private boolean enabled = false; // false -> true

    private VerificationEmailToken emailToken;

    //  public static final int EMAIL_VERIFICATION_DURATION = 24;
    public static final Duration EMAIL_VERIFICATION_DURATION = Duration.ofHours(24);

    /**
     * Generate an email token for the given email address.
     *
     * @param email the email address to generate the token for
     * @return the tokenId in clear text
     */
    public void generateEmailTokenFor(String email) {
        isNotNull(email, "email");

        // var token = generateEmailToken(email, now().plus(24, ChronoUnit.DAYS));
        var token = generateEmailToken(email, now().plus(EMAIL_VERIFICATION_DURATION));

        // Save the token in the account for later verification.
        this.emailToken = token;
    }

    /**
     * Verify the email token for the given tokenId.
     *
     * @param tokenId the tokenId to verify
     * @return the verified email address
     * @throws IllegalArgumentException if verification fails
     */
    public String verifyEmailTokenFor(String tokenId) throws IllegalArgumentException {
        isNotNull(tokenId, "tokenId");
        isNotNull(emailToken, "token");

        // Verify the token by checking if it is not expired and if the hashes match.
        // If verification fails, an IllegalArgumentException is thrown.
        // TODO We need our own exception type here. e.g. (TokenVerificationException)
        verifyToken(emailToken, tokenId);

        // Get the email from the token.
        String verifiedEmail = emailToken.getVerificationEmail();

        // Set the email token to null to prevent reuse.
        this.emailToken = null;

        // Return the verified email.
        return verifiedEmail;
    }

    /**
     * Get the email address to verify. Used by the EmailService to send the verification email. See
     * EmailService.sendVerificationEmail(..).
     *
     * @return the email address to verify
     * @throws IllegalArgumentException if the token is null
     */
    public String getVerificationEmail() {
        isNotNull(emailToken, "token");

        return emailToken.getVerificationEmail();
    }

    /**
     * Get the tokenId to verify. Used by the EmailService to send the verification email. See
     * EmailService.sendVerificationEmail(..).
     *
     * @return the tokenId to verify
     * @throws IllegalArgumentException if the token is null
     */
    public String getVerificationEmailToken() {
        isNotNull(emailToken, "token");

        return emailToken.getVerificationEmailToken();
    }
}


// @Getter
// public class Account {
//  @Setter private boolean enabled = false; // false -> true
//
//  private String tokenId = UUID.randomUUID().toString(); // UUID -> null
//
//  public void verifyToken(String tokenId) {
//    isNotNull(this.tokenId, "tokenId");
//    isTrue(this.tokenId.equals(tokenId), "Tokens do not match");
//
//        if (!this.tokenId.equals(tokenId)) {
//          throw new ...
//        }
//
//    this.tokenId = null;
//  }
// }
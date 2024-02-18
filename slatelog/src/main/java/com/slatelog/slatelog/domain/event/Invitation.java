package com.slatelog.slatelog.domain.event;

import com.slatelog.slatelog.security.token.Token;
import lombok.Getter;

@Getter
// @Setter -  codereview
// Dr. Wenz
// setter setzen nur wenn ich sie brauche, recherche: set methoden antipattern zu einem richdomain model
// alternative gute sprechende- namen methoden

public class Invitation {

    // Email address associated with the invitation
    private String email;

    // Token associated with the email for verification
    private Token emailToken;

    // Constructor to create an Invitation with an email and token
    public Invitation(String email, Token emailToken) {
        this.email = email;
        this.emailToken = emailToken;
    }

    // Default constructor (no-args constructor) for Springboot
    // It's marked as protected to limit its accessibility
    protected Invitation(){};
}

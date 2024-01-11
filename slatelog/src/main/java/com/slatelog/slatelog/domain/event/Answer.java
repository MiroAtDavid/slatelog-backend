package com.slatelog.slatelog.domain.event;

import java.time.Instant;

import static com.slatelog.slatelog.foundation.AssertUtil.isNotNull;
import static com.slatelog.slatelog.foundation.AssertUtil.isValidEmail;

/**
 * Represents an answer to a poll from a voter.
 */
public class Answer {
    private String voterEmail;
    private Instant votedAt;
    private VoteOption voteOption;

    // Default constructor for frameworks like Spring Data
    protected Answer() {}

    /**
     * Creates an answer to a poll.
     *
     * @param voterEmail      The email address of the voter.
     * @param votedAt    The timestamp of the vote.
     * @param voteOption The selected voting option (YES, NO, MAYBE).
     */
    public Answer(String voterEmail, Instant votedAt, VoteOption voteOption) {
        this.voterEmail = isValidEmail(voterEmail, "emailEmail");
        this.votedAt = isNotNull(votedAt, "votedAt");
        this.voteOption = isNotNull(voteOption, "voteOption");
    }
}

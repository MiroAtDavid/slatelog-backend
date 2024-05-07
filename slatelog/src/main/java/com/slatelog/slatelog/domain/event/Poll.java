package com.slatelog.slatelog.domain.event;

import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static com.slatelog.slatelog.foundation.AssertUtil.isNotNull;

/**
 * Represents a poll associated with an event.
 * The poll consists of timestamps and corresponding lists of answers.
 */
@Getter
public class Poll {
    /**
     * HashMap storing poll timestamps as keys and lists of answers as values.
     * The structure allows associating each timestamp with the available answers.
     */

    private HashMap<Instant, List<Answer>> pollOptions;

    private boolean pollOpen;

    private Instant pollCloseDate;
    /**
     * Default constructor for Spring Data.
     */
    protected Poll(){}

    /**
     * Creates a new poll with the specified timestamp and corresponding answers.
     *
     * @param pollOptions The HashMap containing timestamps and corresponding answers.
     */
    public Poll(HashMap<Instant, List<Answer>> pollOptions, Instant pollCloseDate) {
        this.pollOptions = isNotNull(pollOptions, "poll options");
        setPollOpen(pollCloseDate);
    }

    public void setPollOpen(Instant pollCloseDate) {
        this.pollOpen = pollCloseDate.isAfter(Instant.now());
    }
}

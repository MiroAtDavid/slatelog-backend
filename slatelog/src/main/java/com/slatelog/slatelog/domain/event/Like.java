package com.slatelog.slatelog.domain.event;


import java.time.Instant;
import java.util.Objects;

/**
 * Represents a like on a post or comment.
 * This class is typically embedded in Post or Comment entities.
 * Learn more about record classes in Java: https://www.developer.com/java/java-record-class/
 */
public record Like(String userId, Instant createdAt) {

    /**
     * Checks if two likes are equal based on the user ID.
     *
     * @param o The object to compare with this like.
     * @return true if the likes have the same user ID, false otherwise.
     */    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(userId, like.userId);
    }

    /**
     * Generates a hash code for the like based on the user ID.
     *
     * @return The hash code of the user ID.
     */    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}

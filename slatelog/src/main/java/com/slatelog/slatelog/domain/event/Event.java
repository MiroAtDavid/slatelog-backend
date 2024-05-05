package com.slatelog.slatelog.domain.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.slatelog.slatelog.domain.BaseEntity;
import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.media.Media;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.slatelog.slatelog.foundation.AssertUtil.*;
import static com.slatelog.slatelog.foundation.EntityUtil.generateUUIDv4;

/**
 * Represents an event with associated details such as title, description, poll, and location.
 */
@Getter
@Setter
@Document("event")
@TypeAlias("event") // Mongodb check
// underscore class check
public class Event extends BaseEntity<String> {

    @Indexed()
    private String userId;
    private String title;
    private @Nullable String description;
    private Poll poll;
    private @Nullable Address location;
    private @Nullable Set<Invitation> invitations;
    private @Nullable List<Media> medias;
    private @Nullable Set<HashTag> hashTags;
    private Set<Like> likes;
    public static final Duration EMAIL_VERIFICATION_DURATION = Duration.ofHours(24);

    /**
     * Default constructor for Spring Data.
     *
     * @param id The unique identifier for the Event.
     */
    @PersistenceCreator
    @JsonCreator
    protected Event(String id) {
        super(id);
    }

    /**
     * Constructor for creating a new Event.
     * @param userId      The user ID to whom the Event belongs.
     * @param title       The title of the Event.
     * @param description The optional description of the Event.
     * @param poll        The poll associated with the Event.
     * @param location    The optional location of the Event.
     * @param invitations  Invitations for the Event.
     * @param medias      Optional media associated with the Event.
     */
    public Event (String userId, String title, @Nullable String description, Poll poll, @Nullable Address location, @Nullable Set<Invitation> invitations, @Nullable List<Media> medias, @Nullable Set<HashTag> hashTags) {
        super(generateUUIDv4());

        isTrue(title != null || medias != null, "text or medias must not be null");
        this.userId = isNotNull(userId, "userId");
        this.title = isNotNull(title, "title");
        this.description = hasMaxTextOrNull(description, 4096, "desctiption");
        this.poll = isNotNull(poll, "poll");
        this.location = location;
        this.medias = hasMaxSizeOrNull(medias, 10, "medias");
        this.hashTags = hasMaxSizeOrNull(hashTags, 10, "hashTags");
        this.likes = new HashSet<>();
        this.invitations = invitations;
    }
}

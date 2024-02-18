package com.slatelog.slatelog.domain.event;

import com.slatelog.slatelog.domain.BaseEntity;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.HashSet;
import java.util.Set;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;
import static com.slatelog.slatelog.foundation.AssertUtil.isNotNull;
import static com.slatelog.slatelog.foundation.EntityUtil.generateUUIDv4;

/**
 * Represents a comment from a user on an event.
 * Each comment is a separate entity to allow for scalability in MongoDB.
 */
@Getter
@ToString
public class Comment extends BaseEntity<String> {

    /**
     * The ID of the event to which this comment belongs.
     * Indexed for efficient MongoDB queries.
     * Indexing provides faster access to data.
     * See: https://stackoverflow.com/questions/1108/how-does-database-indexing-work
     */
    @Indexed(unique = true)
    private String eventId;
    @Indexed (unique = true)
    private String userId;
    private String text;
    private Set<Like> likes;

    // Constructors --------------------------------------------

    /**
     * Constructor for Spring Data to use when creating a new comment from the database into memory.
     * Spring Data utilizes reflection to instantiate this class.
     * See: https://www.youtube.com/watch?v=bhhMJSKNCQY
     */
    protected Comment(String id) {
        super(id);
    }

    /**
     * Constructor for developers to use when creating a new comment in memory.
     *
     * @param eventId The ID of the event to which the comment belongs.
     * @param userId  The ID of the user creating the comment.
     * @param text    The text content of the comment.
     */
    public Comment(String eventId,String postId, String userId, String text) {
        super(generateUUIDv4());

        this.eventId = isNotNull(postId, "eventId");
        this.userId = isNotNull(userId, "userId");
        this.text = hasMaxText(text, 4096, "text");
        this.likes = new HashSet<>();
    }
}
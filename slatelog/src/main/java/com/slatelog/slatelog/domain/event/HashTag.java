package com.slatelog.slatelog.domain.event;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;

// What is a record class in Java?
// https://www.developer.com/java/java-record-class/

/** Hashtag of a post. */

// This class in inlined in Post.
public record HashTag(String value) {
    // Constructor with validation
    public HashTag {
        hasMaxText(value, 50, "hash tag");
    }
}

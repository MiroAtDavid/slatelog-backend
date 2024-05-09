package com.slatelog.slatelog.domain.event;

import jakarta.annotation.Nullable;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;
import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxTextOrNull;

// What is a record class in Java?
// https://www.developer.com/java/java-record-class/

/** Hashtag of a post. */

// This class in inlined in Post.
public record HashTag(@Nullable String value) {
    // Constructor with validation
    public HashTag {
        hasMaxTextOrNull(value, 50, "hash tag");
    }
}

package com.slatelog.slatelog.domain.media;


import java.time.Instant;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;
import static com.slatelog.slatelog.foundation.AssertUtil.hasMinSize;

// What is a record class in Java?
// https://www.developer.com/java/java-record-class/

/**
 * Media Information.
 *
 * <p>The media information of a media in MongoDB.<br>
 * The binary data of the media is stored in MongoDB GridFS.
 */

// api/media/dog.jpg
// <img src="/api/media/{id}">
// <img src="/api/media/dog.jpg">
// <img src="/api/media/uuidv4">

// This class in inlined in Post or Message.
public record Media(
        String id,
        Instant createdAt,
        String filename,
        String mimeType, // e.g. image/jpeg, image/png, video/mp4
        long size,
        int width,
        int height) {
    // Constructor with validation
    public Media {
        hasMaxText(filename, 255, "filename");
        hasMaxText(mimeType, 50, "mimeType");
        hasMinSize(size, 1, "size");
        hasMinSize(width, 1, "width");
        hasMinSize(height, 1, "height");
    }
}
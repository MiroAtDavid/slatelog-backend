package com.slatelog.slatelog.faker;

import com.slatelog.slatelog.domain.media.Media;
import net.datafaker.Faker;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public class MediaFaker {
    private static final Faker faker = new Faker();

    private static final String FIXED_MEDIA_ID = "meeting.jpg";

    public static Media createMedia() {

        String id = FIXED_MEDIA_ID;
        Instant createdAt = Instant.now();
        String filename = FIXED_MEDIA_ID;
        String mimeTye = "image/jpeg";
        long size = faker.number().numberBetween(1024, 4096);
        int width = faker.number().numberBetween(640, 1920);
        int height = faker.number().numberBetween(480, 1080);

        return new Media(id, createdAt, filename, mimeTye, size, width, height);
    }

    public static List<Media> createMedias(int n) {
        return Stream.generate(MediaFaker::createMedia).limit(n).toList();
    }

    // for testing
    public static void main(String[] args) {

        var medias = createMedias(1);
        medias.forEach(System.out::println);
    }
}

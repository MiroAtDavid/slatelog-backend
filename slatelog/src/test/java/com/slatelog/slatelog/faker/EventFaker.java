package com.slatelog.slatelog.faker;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;
import com.slatelog.slatelog.domain.media.Media;
import net.datafaker.Faker;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EventFaker {

    private static final Faker faker = new Faker();
    private static final int NUM_HASH_TAGS = 3;

    public static Event createEvent(String userId) {

        // Create Event Title for Event
        String eventTitle = faker.lorem().word().toUpperCase();

        // Create Event Description for Event
        var description = faker.lorem().sentence();

        // Create Address for Event
        String cityName = faker.address().city();
        String streetName = faker.address().streetName();
        String country = faker.address().country();
        String zipCode = faker.address().zipCode();
        var eventAddress = new Address(streetName, cityName, country, zipCode);

        // Create Poll data for the Event
        // Three possible dates for the Event
        Instant now = Instant.now();
        long randomHours = ThreadLocalRandom.current().nextLong(24, 77); // Random number of hours between 24 and 76
        Duration duration = Duration.ofHours(randomHours);
        Instant eventDateOne = now.plus(duration).truncatedTo(ChronoUnit.MINUTES);;

        long randomHours1 = ThreadLocalRandom.current().nextLong(24, 77); // Random number of hours between 24 and 76
        Duration duration1 = Duration.ofHours(randomHours1);
        Instant eventDateTwo = now.plus(duration1).truncatedTo(ChronoUnit.MINUTES);;

        long randomHours2 = ThreadLocalRandom.current().nextLong(24, 77); // Random number of hours between 24 and 76
        Duration duration2 = Duration.ofHours(randomHours2);
        Instant eventDateThree = now.plus(duration2).truncatedTo(ChronoUnit.MINUTES);;

        // Emails for answers
        String email1 = faker.internet().emailAddress();
        String email2 = faker.internet().emailAddress();
        String email3 = faker.internet().emailAddress();

        // Vote Option
        VoteOption voteOption1 = VoteOption.Yes;
        VoteOption voteOption2 = VoteOption.No;
        VoteOption voteOption3 = VoteOption.Maybe;

        List<VoteOption> votesOptions = new ArrayList<>();
        votesOptions.add(voteOption1);
        votesOptions.add(voteOption2);
        votesOptions.add(voteOption3);

        Random r = new Random();
        int i = r.nextInt(3);

        Answer answer = new Answer(email1, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer2 = new Answer(email1, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer3 = new Answer(email1, Instant.now(), votesOptions.get(i));

        Answer answer4 = new Answer(email2, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer5 = new Answer(email2, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer6 = new Answer(email2, Instant.now(), votesOptions.get(randomNumberGen()));

        Answer answer7 = new Answer(email3, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer8 = new Answer(email3, Instant.now(), votesOptions.get(randomNumberGen()));
        Answer answer9 = new Answer(email3, Instant.now(), votesOptions.get(randomNumberGen()));

        List<Answer> answers = new ArrayList<>();
        List<Answer> answers2 = new ArrayList<>();
        List<Answer> answers3 = new ArrayList<>();

        answers.add(answer);
        answers2.add(answer2);
        answers3.add(answer3);

        answers.add(answer4);
        answers2.add(answer5);
        answers3.add(answer6);

        answers.add(answer7);
        answers2.add(answer8);
        answers3.add(answer9);

        HashMap<Instant, List<Answer>> instantanswersHashMap = new HashMap<>();
        instantanswersHashMap.put(eventDateOne, answers);
        instantanswersHashMap.put(eventDateTwo, answers2);
        instantanswersHashMap.put(eventDateThree, answers3);


        // Create Invitation for the Event
        Instant voteDeadLine = Instant.now().plus(Duration.ofHours(24));

        // Create Poll for the Event
        Poll poll = new Poll(instantanswersHashMap, voteDeadLine);


        Invitation inviteOne = new Invitation(email1, voteDeadLine);
        Invitation inviteTwo = new Invitation(email2, voteDeadLine);
        Invitation inviteThree = new Invitation(email3, voteDeadLine);
        Set<Invitation> invitations = new HashSet<>();
        invitations.add(inviteOne);
        invitations.add(inviteTwo);
        invitations.add(inviteThree);

        // Create Medias
        List<Media> medias = MediaFaker.createMedias(1);
        var hashTags = fakeHashTags(NUM_HASH_TAGS);

        var event = new Event(userId, eventTitle, description, poll, eventAddress, invitations, medias, hashTags);

        // setBaseEntityField(user, "createdAt", Instant.now());
        // setBaseEntityField(user, "lastModifiedAt", Instant.now());
        // setBaseEntityField(user, "version", null);

        return event;
    }


    public static int randomNumberGen() {
        Random r = new Random();
        int i = r.nextInt(3);
        return i;
    }

    public static List<Event> createEvents(String userId, int n) {
        return Stream.generate(() -> createEvent(userId)).limit(n).toList();
    }

    // for testing
    public static void main(String[] args) {

        var events = createEvents("userId", 10);
        events.forEach(System.out::println);
    }

    private static Set<HashTag> fakeHashTags(int n) {
        return Stream.generate(() -> new HashTag(faker.lorem().word()))
                .limit(n)
                .collect(Collectors.toSet());
    }
}

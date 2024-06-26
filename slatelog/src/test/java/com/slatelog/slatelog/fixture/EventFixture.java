package com.slatelog.slatelog.fixture;

import com.slatelog.slatelog.domain.address.Address;
import com.slatelog.slatelog.domain.event.*;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EventFixture {


    public static final String TITLE = "Meeting";
    public static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum";
    public static final VoteOption VOTE_OPTION_1 = VoteOption.Yes;
    public static final VoteOption VOTE_OPTION_2 = VoteOption.No;
    public static final VoteOption VOTE_OPTION_3 = VoteOption.Maybe;
    public static final String STREET = "Spengergasse 7";
    public static final String CITY = "Wien";
    public static final String STATE = "Austria";
    public static final String ZIPCODE = "1050";
    public static HashMap<Instant, List<Answer>> POLLMAP = new HashMap<>();
    public static Invitation INVITATION;
    public static Invitation INVITATION2;
    public static final String EMAIL = "wenz@spengergasse.at";
    public static final List<Answer> ANSWERS = new ArrayList<>();
    public static final List<Answer> ANSWERS2 = new ArrayList<>();
    public static final List<Answer> ANSWERS3 = new ArrayList<>();
    public static Instant INSTANT_KEY = Instant.now().plus(Duration.ofDays(5));
    public static final Instant INST = INSTANT_KEY.truncatedTo(ChronoUnit.MINUTES);
    public static Instant INSTANT_KEY1 = Instant.now().plus(Duration.ofDays(6));
    public static final Instant INST1 = INSTANT_KEY1.truncatedTo(ChronoUnit.MINUTES);
    public static Instant INSTANT_KEY2 = Instant.now().plus(Duration.ofDays(7));
    public static final Instant INST2 = INSTANT_KEY2.truncatedTo(ChronoUnit.MINUTES);
    public static final Set<Invitation> INVITATIONS = new HashSet<>();
    public static final Set<HashTag> HASH_TAGS = new HashSet<>();
    public static byte[] ICSFILEDATA; // Binary data to store ICS file in MongoDB
    public static final Instant VOTEDEADLINE = Instant.now().plus(Duration.ofHours(24));


    public static Event createEvent() {

        // Creates Event-organiser
        var user = UserFixture.createUser();

        // Creating Location for Event
        var location = new Address(STREET,CITY,STATE,ZIPCODE);

        // Get the current Instant
        Instant currentInstant = Instant.now();

        // Answers for each Poll Option for the Event
        var answer1 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_1);
        var answer2 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_2);
        var answer3 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_3);
        var answer4 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_2);
        var answer5 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_3);
        var answer6 = new Answer(UserFixture.EMAIL, currentInstant, VOTE_OPTION_1);

        ANSWERS.add(answer1);
        ANSWERS.add(answer2);
        ANSWERS.add(answer3);

        ANSWERS2.add(answer4);
        ANSWERS2.add(answer5);
        ANSWERS2.add(answer6);

        ANSWERS3.add(answer2);
        ANSWERS3.add(answer5);
        ANSWERS3.add(answer3);

        // Event poll
        POLLMAP.put(INST, ANSWERS);
        POLLMAP.put(INST1, ANSWERS2);
        POLLMAP.put(INST2, ANSWERS2);
        var poll = new Poll(POLLMAP, INSTANT_KEY2);

        // Event Invitations
        INVITATION = new Invitation(user.getEmail(),VOTEDEADLINE);
        INVITATION2 = new Invitation(EMAIL,VOTEDEADLINE);
        INVITATIONS.add(INVITATION);
        INVITATIONS.add(INVITATION2);


        // Create HashTags
        HashTag tag1 = new HashTag("pakao");
        HashTag tag2 = new HashTag("hell");
        HashTag tag3 = new HashTag("hoelle");

        HASH_TAGS.add(tag1);
        HASH_TAGS.add(tag2);
        HASH_TAGS.add(tag3);


        // Creating the Event
        Event CREATEDEVENT;
        CREATEDEVENT = new Event(user.getId(), TITLE, DESCRIPTION, poll, location, INVITATIONS, null, HASH_TAGS);

        return CREATEDEVENT;
    }
}




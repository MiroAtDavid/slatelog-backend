
package com.slatelog.slatelog.persistence;


import com.slatelog.slatelog.config.MongoConfig;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.event.Invitation;
import com.slatelog.slatelog.fixture.EventFixture;
import com.slatelog.slatelog.fixture.UserFixture;
import com.slatelog.slatelog.persistance.EventRepository;
import com.slatelog.slatelog.service.IcsCalendarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;




@DataMongoTest
@Import(MongoConfig.class)
public class
EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event eventSaved;

    @BeforeEach
    public void setup(){
        var event = EventFixture.createEvent();
        eventRepository.deleteAll();
        eventSaved = eventRepository.save(event);
    }

    @Test
    public void saveEvent_shouldReturnSavedEvent() {
        // Given
        // @BeforeEach

        // When
        // @BeforeEach

        // Then
        assertThat(eventSaved, notNullValue());
    }

    @Test
    public void saveEvent_shouldSetAuditFields() {
        // Given
        // @BeforeEach

        // When
        // @BeforeEach

        // Then
        assertThat(eventSaved.getCreatedAt(), notNullValue());
        assertThat(eventSaved.getLastModifiedAt(), notNullValue());
        assertThat(eventSaved.getVersion(), notNullValue());
        assertThat(eventSaved.getVersion(), equalTo(0L));
    }

    @Test
    public void findById_shouldReturnEvent_whenEventExists() {
        // Given
        // @BeforeEach

        // When
        Optional<Event> eventFound = eventRepository.findById(eventSaved.getId());

        // Then
        assertThat(eventFound.isPresent(), is(true));
        assertThat(eventFound.get().getId(), equalTo(eventSaved.getId()));
   }

    @Test
    public void findByTitle_shouldReturnEvent_whenEventExists() {
        // Given
        // @BeforeEach

        // When
        Optional<Event> userFound = eventRepository.findByTitle(eventSaved.getTitle());

        // Then
        assertThat(userFound.isPresent(), is(true));
        assertThat(userFound.get().getTitle(), equalTo(eventSaved.getTitle()));
    }
    @Test
    public void createIcsFileFromEvent() {

        Set<Instant> eventDates = eventSaved.getPoll().getPollOptions().keySet();
        List<Instant> instantList = new ArrayList<>();
        instantList.addAll(eventDates);

        LocalDateTime eventCreatedAt =  LocalDateTime.ofInstant(eventSaved.getCreatedAt(), ZoneId.systemDefault());
        LocalDateTime eventEndTime = eventCreatedAt.plusHours(1);
        String eventLocationState = eventSaved.getLocation().state().toString();
        String eventLocationCity = eventSaved.getLocation().city().toString();
        String eventLocationZip = eventSaved.getLocation().zipCode().toString();
        String eventLocationStreet = eventSaved.getLocation().street() .toString();
        String eventLocation = eventLocationState + ", " + eventLocationCity + " " + eventLocationZip + ", " + eventLocationStreet;

        String [] invitationEmails = eventSaved.getInvitations().stream()
                        .map(Invitation::getEmail)
                .toArray(String[]::new);
        IcsCalendarService.generateICSFile(eventSaved.getTitle(), instantList,
                eventSaved.getDescription(), eventLocation, UserFixture.EMAIL, invitationEmails,
                "myicsfile1.ics");


    }
}

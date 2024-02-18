package com.slatelog.slatelog.persistence;


import com.slatelog.slatelog.config.MongoConfig;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.fixture.EventFixture;
import com.slatelog.slatelog.persistance.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;




@DataMongoTest
@Import(MongoConfig.class)
public class EventRepositoryTest {

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

/*    @Test
    public void findById_shouldReturnEvent_whenEventExists() {
        // Given
        // @BeforeEach

        // When
        Optional<Event> eventFound = eventRepository.findById(eventSaved.getId());

        // Then
        assertThat(eventFound.isPresent(), is(true));
        assertThat(eventFound.get().getId(), equalTo(eventSaved.getId()));
   }*/
}

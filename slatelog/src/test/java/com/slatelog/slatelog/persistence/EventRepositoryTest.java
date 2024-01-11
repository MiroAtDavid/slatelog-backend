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
        // Bruh
        assertThat(eventSaved, notNullValue());
    }
}

package com.slatelog.slatelog.presentation.mappers;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.fixture.EventFixture;
import com.slatelog.slatelog.presentation.views.EventViewMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class EventViewMapperTest {

    // SUT_shouldXXX_whenXXX

    @Test
    public void toEventView_shouldMapEventToEventView() {

        // Given
        Event event = EventFixture.createEvent();
        EventViewMapper mapper = EventViewMapper.INSTANCE;

        // When
        var eventView = mapper.toEventView(event);

        // Then
        assertThat(eventView, notNullValue());
        assertThat(eventView.id(), equalTo(event.getId()));
    }
}
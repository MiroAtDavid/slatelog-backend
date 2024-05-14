package com.slatelog.slatelog.persistance;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.service.UserQueryService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    Optional<Event> findByTitle(String title);

    Event getEventById(String eventId);

    List<Event> findByUserId(String userid);

    // List<Event> findByMailSent(boolean mailSent);

    List<Event> findEventsByMailSentFalse();

}
package com.slatelog.slatelog.persistance;

import com.slatelog.slatelog.domain.event.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {

    Optional<Event> findByTitle(String title);

    boolean existsByTitle(String title);
}

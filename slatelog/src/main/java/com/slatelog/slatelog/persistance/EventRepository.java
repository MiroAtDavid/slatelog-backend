package com.slatelog.slatelog.persistance;

import com.slatelog.slatelog.domain.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends MongoRepository<Event, String>, CustomEventRepository {

    Optional<Event> findByTitle(String title);

    Event getEventById(String eventId);

    List<Event> findByUserId(String userid);

    List<Event> findEventsByMailSentFalse();

    Event findByIdAndUserId(String eventId, String userId);

    List<Event> findByPollPollCloseDateAfterAndMailSent(Instant date, boolean hasEmailSent);
}

interface CustomEventRepository {

    List<Event> oidaGibOisHer();


}

 @Repository
 @RequiredArgsConstructor
class CustomEventRepositoryImpl implements CustomEventRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Event> oidaGibOisHer() {

       // how to write a custom mongo query

        return null;
    }
}




package com.slatelog.slatelog.faker;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.persistance.UserRepository;
import com.slatelog.slatelog.persistance.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.slatelog.slatelog.faker.SlatelogFaker.fakeUsersWithEvents;

@Service
@RequiredArgsConstructor
public class FakeDataLoader {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public void loadFakeData(int numberOfUsers, int numberOfEvents) {

        Map<User, List<Event>> userEventMap = fakeUsersWithEvents(numberOfUsers, numberOfEvents);

        // Returns all users from the map
        Set<User> allUsers = userEventMap.keySet();

        // Returns all posts from the map as a collection of lists
        // Collection<List<Post>> posts = userPostMap.values();

        // We have to flatten the list of posts into a single list
        List<Event> allEvents = userEventMap.values().stream().flatMap(events -> events.stream()).toList();

        System.out.println("Users: " + allUsers);
        System.out.println("Events: " + allEvents);

        userRepository.saveAll(allUsers);
        eventRepository.saveAll(allEvents);
    }
}

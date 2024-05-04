<<<<<<< HEAD
package com.slatelog.slatelog.faker;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;

import java.util.List;
import java.util.Map;

import static com.slatelog.slatelog.faker.EventFaker.createEvents;
import static com.slatelog.slatelog.faker.UserFaker.createUsers;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class SlatelogFaker {
    public static Map<User, List<Event>> fakeUsersWithEvents(int numberOfUsers, int numberOfEvents) {

        // Create n users
        return createUsers(numberOfUsers).stream()
                // Map each user to a list of events
                .collect(toMap(identity(), user -> createEvents(user.getId(), numberOfEvents)));
    }

    // for testing
    public static void main(String[] args) {

        int numberOfUsers = 3;
        int numberOfEvents = 2;
        Map<User, List<Event>> userListMap = fakeUsersWithEvents(numberOfUsers, numberOfEvents);
        System.out.println(userListMap);
    }
}
=======
package com.slatelog.slatelog.faker;public class SlatelogFaker {
}
>>>>>>> 11a96e5 (invitaitonEmail(emailService))

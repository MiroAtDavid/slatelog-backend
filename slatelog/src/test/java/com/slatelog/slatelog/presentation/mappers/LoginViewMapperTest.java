package com.slatelog.slatelog.presentation.mappers;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.fixture.EventFixture;
import com.slatelog.slatelog.fixture.UserFixture;
import com.slatelog.slatelog.presentation.views.LoginViewMapper;
import com.slatelog.slatelog.presentation.views.Views;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class LoginViewMapperTest {

    // SUT_shouldXXX_whenXXX

    @Test
    public void toLoginView_shouldMapParamsToLoginView() {

        // Given
        User user = UserFixture.createUser();
        // List<Post> posts = Stream.generate(() -> PostFixture.createTextPost()).limit(3).toList();
        List<Event> events = Stream.generate(EventFixture::createEvent).limit(3).toList();
        // List<Post> posts = Arrays.asList(PostFixture.createTextPost());
        LoginViewMapper mapper = LoginViewMapper.INSTANCE;

        // When
        Views.LoginView loginView = mapper.toLoginView(user, events);

        // Then
        assertThat(loginView, notNullValue());
        assertThat(loginView.user(), notNullValue());
        // assertThat(loginView.posts(), notNullValue());

    }
}

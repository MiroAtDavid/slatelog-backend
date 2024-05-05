package com.slatelog.slatelog.presentation.mappers;

import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.fixture.UserFixture;
import com.slatelog.slatelog.presentation.views.UserViewMapper;
import com.slatelog.slatelog.presentation.views.Views;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserViewMapperTest {

    // SUT_shouldXXX_whenXXX
    @Test
    public void toUserView_shouldMapUserToUserView() {

        // Given
        User user = UserFixture.createUser();
        UserViewMapper mapper = UserViewMapper.INSTANCE;

        // When
        Views.UserView userView = mapper.toUserView(user);

        // Then
        assertThat(userView, notNullValue());
        assertThat(userView.id(), equalTo(user.getId()));
    }
}

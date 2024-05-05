package com.slatelog.slatelog.presentation.views;

import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.presentation.views.Views.LoginView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// We can use multiple mappers in one mapper
@Mapper(uses = {UserViewMapper.class, EventViewMapper.class})
public interface LoginViewMapper {

    LoginViewMapper INSTANCE = Mappers.getMapper(LoginViewMapper.class);

    LoginView toLoginView(User user, List<Event> events);
}

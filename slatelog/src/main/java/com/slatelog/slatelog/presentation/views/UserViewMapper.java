package com.slatelog.slatelog.presentation.views;

import com.slatelog.slatelog.domain.user.User;
import com.slatelog.slatelog.presentation.views.Views.UserView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

// MapStruct maps the User Domain Model into a User view via the `toUserView`

@Mapper
public interface UserViewMapper {

    UserViewMapper INSTANCE = Mappers.getMapper(UserViewMapper.class);

    UserView toUserView(User user);
}
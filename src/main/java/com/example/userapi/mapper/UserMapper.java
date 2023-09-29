package com.example.userapi.mapper;

import com.example.userapi.config.MapperConfig;
import com.example.userapi.dto.UserRegistrationDto;
import com.example.userapi.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    User toUser(UserRegistrationDto requestDto);
}

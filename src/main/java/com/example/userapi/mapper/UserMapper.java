package com.example.userapi.mapper;

import com.example.userapi.config.MapperConfig;
import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(UserRequestDto requestDto);

    UserResponseDto toResponseDto(User user);
}

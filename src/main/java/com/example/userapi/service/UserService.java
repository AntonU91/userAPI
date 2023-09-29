package com.example.userapi.service;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateDto;
import java.time.LocalDate;
import java.util.List;


public interface UserService {
    public void create(UserRequestDto userRequestDto);

    void updateAllProperties(UserRequestDto userRequestDto, Long userId);

    void updateSomeProperties(UserUpdateDto userUpdateDto, Long userId);

    void delete(Long userId);

    List<UserResponseDto> getUsersBySpecifedDateBirthRange(LocalDate from, LocalDate to);
}

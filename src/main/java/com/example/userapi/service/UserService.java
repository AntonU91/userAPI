package com.example.userapi.service;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;


public interface UserService {
    void create(UserRequestDto userRequestDto);

    void updateAllProperties(UserRequestDto userRequestDto, Long userId);

    void updateSomeProperties(UserUpdateDto userUpdateDto, Long userId);

    void delete(Long userId);

    List<UserResponseDto> getUsersBySpecifedBirthDateRange(LocalDate from, LocalDate to, Pageable pageable);
}

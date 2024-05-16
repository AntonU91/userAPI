package com.example.userapi.service.impl;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateRequestDto;
import com.example.userapi.exception.InvalidDateRangeException;
import com.example.userapi.exception.InvalidUserBirthDateException;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${user.minimal.age}")
    private int minimalAge;

    @Override public void create(UserRequestDto userRequestDto) {
        User user = userMapper.toUser(userRequestDto);
        userRepository.save(user);
    }

    @Override public void updateAllProperties(UserRequestDto userRequestDto, Long userId) {
        checkAndReturnUserById(userId);
        User userToUpdate = userMapper.toUser(userRequestDto);
        userToUpdate.setId(userId);
        userRepository.save(userToUpdate);
    }

    @Override public void updateSomeProperties(UserUpdateRequestDto userUpdateRequestDto, Long userId) {
        User user = checkAndReturnUserById(userId);
        User updatedUser = updateUser(user, userUpdateRequestDto);
        userRepository.save(updatedUser);
    }

    @Override public void delete(Long userId) {
        User user = checkAndReturnUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDto> getUsersBySpecifedBirthDateRange(LocalDate from, LocalDate to, Pageable pageable) throws InvalidDateRangeException {
        if (!to.isAfter(from)) {
            throw new InvalidDateRangeException("Invalid date range");
        }
        Page<User> userList = userRepository.findAllByBirthDateRange(from, to, pageable);
        return userList.stream()
                       .map(userMapper::toResponseDto)
                       .toList();
    }


    private User checkAndReturnUserById(Long userId) {
        return userRepository.findById(userId)
                       .orElseThrow(() -> new EntityNotFoundException(
                               "Can not find user with id: " + userId));
    }

    private User updateUser(User user, UserUpdateRequestDto userUpdateRequestDto) throws InvalidUserBirthDateException {
        checkAndUpdateBirthDate(user, userUpdateRequestDto);
        if (userUpdateRequestDto.getAddress() != null) {
            user.setAddress(userUpdateRequestDto.getAddress());
        }
        if (userUpdateRequestDto.getEmail() != null) {
            user.setEmail(userUpdateRequestDto.getEmail());
        }
        if (userUpdateRequestDto.getFirstName() != null) {
            user.setFirstName(userUpdateRequestDto.getFirstName());
        }
        if (userUpdateRequestDto.getLastName() != null) {
            user.setLastName(userUpdateRequestDto.getLastName());
        }

        if (userUpdateRequestDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateRequestDto.getPhoneNumber());
        }
        return user;
    }

    private void checkAndUpdateBirthDate(User user, UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getBirthDate() != null) {
            if (isValidBirthDate(userUpdateRequestDto.getBirthDate())) {
                user.setBirthDate(userUpdateRequestDto.getBirthDate());
            } else {
                throw new InvalidUserBirthDateException("Invalid user birth date. Should be at least 19 years");
            }
        }
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() > minimalAge;
    }

}

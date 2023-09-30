package com.example.userapi.service.impl;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateDto;
import com.example.userapi.exception.InvalidDateRangeException;
import com.example.userapi.exception.InvalidUserBirhDateException;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
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

    @Override public void updateSomeProperties(UserUpdateDto userUpdateDto, Long userId) {
        User user = checkAndReturnUserById(userId);
        User updatedUser = updateUser(user, userUpdateDto);
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

    private User updateUser(User user, UserUpdateDto userUpdateDto) throws InvalidUserBirhDateException {
        checkAndUpdateBirthDate(user, userUpdateDto);
        if (userUpdateDto.getAddress() != null) {
            user.setAddress(userUpdateDto.getAddress());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        if (userUpdateDto.getFirstName() != null) {
            user.setFirstName(userUpdateDto.getFirstName());
        }
        if (userUpdateDto.getLastName() != null) {
            user.setLastName(userUpdateDto.getLastName());
        }

        if (userUpdateDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userUpdateDto.getPhoneNumber());
        }
        return user;
    }

    private void checkAndUpdateBirthDate(User user, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getBirthDate() != null) {
            if (isValidBirthDate(userUpdateDto.getBirthDate())) {
                user.setBirthDate(userUpdateDto.getBirthDate());
            } else {
                throw new InvalidUserBirhDateException("Invalid user birth date. Should be at least 19 years");
            }
        }
    }

    private boolean isValidBirthDate(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() > minimalAge;
    }

}

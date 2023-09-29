package com.example.userapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.exception.InvalidDateRangeException;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.impl.UserServiceImpl;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    public static final long USER_ID = 1L;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void createUser_ValidUserRequestDto_CreateUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail("test@user.net");
        user.setBirthDate(LocalDate.of(1999, 2, 25));
        user.setFirstName("Test name");
        user.setLastName("Test surname");

        UserRequestDto validUserRequestDto = new UserRequestDto();
        validUserRequestDto.setEmail("test@user.net");
        validUserRequestDto.setBirthDate(LocalDate.of(1999, 2, 25));
        validUserRequestDto.setFirstName("Test name");
        validUserRequestDto.setLastName("Test surname");
        when(userMapper.toUser(validUserRequestDto)).thenReturn(user);

        userService.create(validUserRequestDto);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateAllUserProperties_ValidUserRequestDto_UpdateAllUserProperties() {
        UserRequestDto validUserRequestDto = new UserRequestDto();
        validUserRequestDto.setEmail("test@user.net");
        validUserRequestDto.setBirthDate(LocalDate.of(1999, 2, 25));
        validUserRequestDto.setFirstName("Test name");
        validUserRequestDto.setLastName("Test surname");

        User existingUser = new User();
        existingUser.setId(USER_ID);
        existingUser.setEmail("test@user.net");
        existingUser.setBirthDate(LocalDate.of(1999, 2, 25));
        existingUser.setFirstName("Test name");
        existingUser.setLastName("Test surname");

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));
        User updatedUser = new User();
        updatedUser.setEmail("test@user.net");
        updatedUser.setBirthDate(LocalDate.of(2001, 2, 25));
        updatedUser.setFirstName("Update name");
        updatedUser.setLastName("Update surname");

        when(userMapper.toUser(validUserRequestDto)).thenReturn(updatedUser);
        userService.updateAllProperties(validUserRequestDto, USER_ID);

        assertEquals(USER_ID, updatedUser.getId());
        assertEquals(USER_ID, updatedUser.getId());
        assertEquals("Update name", updatedUser.getFirstName());
        assertEquals("Update surname", updatedUser.getLastName());
        assertEquals(LocalDate.of(2001, 2, 25), updatedUser.getBirthDate());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void deleteUserById_ExistedId_DeleteUser() {
        User existingUser = new User();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));
        userService.delete(USER_ID);
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    public void getUsersBySpecifiedBirthDateRange_ValidBirthDataRange_ReturnUserResponseDtoList() throws InvalidDateRangeException {
        User user1 = new User();
        User user2 = new User();
        UserResponseDto userResponseDto1 = new UserResponseDto();
        UserResponseDto userResponseDto2 = new UserResponseDto();
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);
        Pageable pageable = Pageable.unpaged();
        Page<User> userPage = new PageImpl<>(Arrays.asList(user1, user2));

        when(userRepository.findAllByBirthDateRange(from, to, pageable)).thenReturn(userPage);
        List<UserResponseDto> userResponseList =
                Arrays.asList(userResponseDto1, userResponseDto2);
        when(userMapper.toResponseDto(user1)).thenReturn(userResponseDto1);
        when(userMapper.toResponseDto(user2)).thenReturn(userResponseDto2);

        List<UserResponseDto> result =
                userService.getUsersBySpecifedBirthDateRange(from, to, pageable);

        assertEquals(userResponseList.size(), result.size());
    }

}
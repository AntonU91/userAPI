package com.example.userapi.controller;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateDto;
import com.example.userapi.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    private void create(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.create(userRequestDto);
    }

    @PutMapping(value = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    private void updateAllProperties(
            @RequestBody @Valid UserRequestDto userRequestDto,
            @PathVariable(name = "userId") Long userId) {
        userService.updateAllProperties(userRequestDto, userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    private void updateSomeProperties(@RequestBody @Valid UserUpdateDto userUpdateDto,
            @PathVariable(name = "userId") Long userId) {
        userService.updateSomeProperties(userUpdateDto, userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    private void delete(@PathVariable(name = "userId") Long userId) {
        userService.delete(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    private List<UserResponseDto> getUsersInSpecifedDateBirthRange(
            @RequestParam(name = "from", required = true) LocalDate from,
            @RequestParam(name = "to", required = true) LocalDate to, Pageable pageable) {
        return userService.getUsersBySpecifedBirthDateRange(from, to, pageable);
    }
}

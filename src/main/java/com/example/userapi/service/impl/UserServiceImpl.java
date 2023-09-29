package com.example.userapi.service.impl;

import com.example.userapi.dto.UserRegistrationDto;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.example.userapi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override public void create(UserRegistrationDto userRegistrationDto) {
        User user = userMapper.toUser(userRegistrationDto);
        userRepository.save(user);
    }
}

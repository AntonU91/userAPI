package com.example.userapi.service;

import com.example.userapi.dto.UserRegistrationDto;
import org.springframework.stereotype.Service;


public interface UserService {
    public void create(UserRegistrationDto userRegistrationDto);
}

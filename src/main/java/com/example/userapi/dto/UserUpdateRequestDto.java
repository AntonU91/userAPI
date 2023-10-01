package com.example.userapi.dto;

import jakarta.validation.constraints.Email;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserUpdateRequestDto {
    @Email
    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}

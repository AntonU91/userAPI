package com.example.userapi.dto;

import com.example.userapi.validation.BirthDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    @BirthDate
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}

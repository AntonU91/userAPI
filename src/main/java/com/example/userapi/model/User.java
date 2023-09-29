package com.example.userapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;


    @Column(name = "last_name", nullable = false)
    private String lastName;


    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "address")
    private String address;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    @Column(name = "phone_number")
    private String phoneNumber;

}

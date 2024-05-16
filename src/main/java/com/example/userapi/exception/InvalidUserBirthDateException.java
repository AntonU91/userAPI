package com.example.userapi.exception;

public class InvalidUserBirthDateException extends RuntimeException{
    public InvalidUserBirthDateException(String message) {
        super(message);
    }
}

package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Exception{
    private static String errorMessage = "Could not find the user with the id you have provided!";

    public UserNotFoundException() {
        super(errorMessage);
    }
    public UserNotFoundException(String msg) {
        super(msg);
    }
}

package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotInGroup extends Exception{
    private static String msg = "The user is not in a group!";

    public UserNotInGroup() {
        super(msg);
    }
}

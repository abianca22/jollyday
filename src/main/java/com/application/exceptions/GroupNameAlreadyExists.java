package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
public class GroupNameAlreadyExists extends Exception{
    private static String msg = "There is already a group with this name. The group name should be unique!";

    public GroupNameAlreadyExists() {
        super(msg);
    }
}

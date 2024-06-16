package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
public class LeaderAlreadyExistsForAnotherGroup extends Exception{
    private static String msg = "This user is already leader to another group!";
    public LeaderAlreadyExistsForAnotherGroup() {
        super(msg);
    }
}

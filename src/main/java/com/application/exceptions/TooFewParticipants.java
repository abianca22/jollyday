package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TooFewParticipants extends Exception{
    private static String msg = "Event cannot be created: too few participants!";
    public TooFewParticipants() {
        super(msg);
    }
}

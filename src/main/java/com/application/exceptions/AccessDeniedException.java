package com.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedException extends Exception{
    private static String errorMessage = "Access denied!";

    public AccessDeniedException() {
        super(errorMessage);
    }
}

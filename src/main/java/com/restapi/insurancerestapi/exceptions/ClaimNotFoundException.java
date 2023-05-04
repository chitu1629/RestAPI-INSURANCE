package com.restapi.insurancerestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ClaimNotFoundException extends RuntimeException {
    public ClaimNotFoundException(String s) {
        super(s);
    }
}

package com.restapi.insurancerestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InsurancePolicyNotFoundException extends RuntimeException {
    public InsurancePolicyNotFoundException(String s) {
        super(s);
    }
}

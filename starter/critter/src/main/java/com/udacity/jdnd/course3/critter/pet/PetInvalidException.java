package com.udacity.jdnd.course3.critter.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Pet")
public class PetInvalidException extends RuntimeException{
    public PetInvalidException(String message) {
        super(message);
    }
}

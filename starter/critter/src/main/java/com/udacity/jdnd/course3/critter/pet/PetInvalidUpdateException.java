package com.udacity.jdnd.course3.critter.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Pet invalid update")
public class PetInvalidUpdateException extends RuntimeException{
}

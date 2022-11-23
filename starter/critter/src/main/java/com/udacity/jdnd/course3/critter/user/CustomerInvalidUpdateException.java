package com.udacity.jdnd.course3.critter.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Customer invalid update")
public class CustomerInvalidUpdateException extends RuntimeException{
}

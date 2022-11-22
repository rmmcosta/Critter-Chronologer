package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Schedule")
public class ScheduleInvalidException extends RuntimeException{
    public ScheduleInvalidException(String message) {
        super(message);
    }
}

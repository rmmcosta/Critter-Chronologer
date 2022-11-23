package com.udacity.jdnd.course3.critter.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Schedule invalid update")
public class ScheduleInvalidUpdateException extends RuntimeException{
}

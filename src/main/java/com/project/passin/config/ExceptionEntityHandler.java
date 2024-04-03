package com.project.passin.config;

import com.project.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import com.project.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import com.project.passin.domain.checkin.exception.CheckInAlreadyExistException;
import com.project.passin.domain.event.exceptions.EventFullException;
import com.project.passin.domain.event.exceptions.EventNotFoundException;
import com.project.passin.dto.general.ErroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionEntityHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity handleEventNotFound(EventNotFoundException exception){

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity handleAttendeeNotFound(AttendeeNotFoundException exception){

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AttendeeAlreadyExistException.class)
    public ResponseEntity handleAttendeeAlreadyExist(AttendeeAlreadyExistException exception){

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(CheckInAlreadyExistException.class)
    public ResponseEntity handleCheckInAlreadyExist(CheckInAlreadyExistException exception){

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(EventFullException.class)
    public ResponseEntity<ErroResponseDto> handleEventFull(EventFullException exception){

        return ResponseEntity.badRequest().body(new ErroResponseDto(exception.getMessage()));
    }
}

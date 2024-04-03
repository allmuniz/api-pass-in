package com.project.passin.domain.checkin.exception;

public class CheckInAlreadyExistException extends RuntimeException{

    public CheckInAlreadyExistException(String message){
        super(message);
    }
}

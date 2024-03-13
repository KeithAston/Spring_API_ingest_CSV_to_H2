package com.ka.csvtoinmemoryh2exercise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidFileTypeException extends Exception{
    public InvalidFileTypeException(String message) { super(message);}
}

package com.ka.csvtoinmemoryh2exercise.controllers;

import com.ka.csvtoinmemoryh2exercise.exceptions.InvalidFileTypeException;
import com.ka.csvtoinmemoryh2exercise.exceptions.ResourceNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler(){ super(); }

    @ExceptionHandler({ BadRequestException.class, InvalidFileTypeException.class })
    public ResponseEntity<Object> handleInternal(final Exception ex, final WebRequest request) {
        final String bodyOfResponse = "Bad Request, please check details of request and try again";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<Object> handleInternal(final ResourceNotFoundException ex, final WebRequest request) {
        final String bodyOfResponse = "Resource not found in database. Please check your request and try again";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}

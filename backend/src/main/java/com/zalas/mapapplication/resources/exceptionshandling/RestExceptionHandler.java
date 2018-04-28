package com.zalas.mapapplication.resources.exceptionshandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ElementNotFoundException.class})
    protected ResponseEntity<RestError> handleError(ElementNotFoundException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new RestError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<RestError> handleError(Exception e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(new RestError("Service Error: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

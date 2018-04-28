package com.zalas.mapapplication.resources.exceptionshandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ElementNotFoundException.class})
    protected ResponseEntity<RestError> handleError(ElementNotFoundException e) {
        return new ResponseEntity(new RestError(e.getMessage()), HttpStatus.NOT_FOUND);
    }

}

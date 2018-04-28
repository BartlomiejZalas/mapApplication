package com.zalas.mapapplication.resources.exceptionshandling;


public class RestError {
    private String errorMessage;

    public RestError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

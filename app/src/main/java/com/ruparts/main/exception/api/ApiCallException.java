package com.ruparts.main.exception.api;

public class ApiCallException extends RuntimeException {
    public String details;

    public ApiCallException (String message) {
        super(message);
    }

    public ApiCallException (String message, String details) {
        super(message);
        this.details = details;
    }

    public ApiCallException (String message, Throwable cause) {
        super(message, cause);
    }
}

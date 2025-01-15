package com.ruparts.main.exception.api;

public class ValidationFailedException extends ApiCallException {

    public ValidationFailedException() {
        super("Validation errors returned");
    }

    public ValidationFailedException(Throwable cause) {
        super("Validation errors returned", cause);
    }
}

package com.ruparts.main.exception.api;

public class AccessDeniedException extends ApiCallException {
    public AccessDeniedException() {
        super("Access denied");
    }
    public AccessDeniedException(Throwable cause) {
        super("Access denied", cause);
    }
}

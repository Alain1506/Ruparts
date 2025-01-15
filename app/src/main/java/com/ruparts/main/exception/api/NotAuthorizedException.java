package com.ruparts.main.exception.api;

public class NotAuthorizedException extends ApiCallException {
    public NotAuthorizedException() {
        super("Invalid token");
    }
    public NotAuthorizedException(Throwable cause) {
        super("Invalid token", cause);
    }
}

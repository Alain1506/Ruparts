package com.ruparts.main.exception.api;

public class DecodeException extends ApiCallException {
    public DecodeException() {
        super("Decode error");
    }
    public DecodeException(Throwable cause) {
        super("Decode error: " + cause.getMessage(), cause);
    }
}

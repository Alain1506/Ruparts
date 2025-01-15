package com.ruparts.main.exception.api;

import java.util.Map;

public class SpecificErrorException extends ApiCallException{

    private final String code;
    private final String dataAsString;

    public SpecificErrorException(String message, String code, String dataAsString) {
        super("Сервер вернул ошибку: " + message);
        this.code = code;
        this.dataAsString = dataAsString;
    }

    public String getCode() {
        return code;
    }

    public String getDataAsString() {
        return dataAsString;
    }
}

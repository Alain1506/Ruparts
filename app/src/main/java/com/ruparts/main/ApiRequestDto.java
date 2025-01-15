package com.ruparts.main;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiRequestDto {
    @JsonProperty("id")
    public String id;
    @JsonProperty("action")
    public String action;
    @JsonProperty("data")
    public Object dataObject;

    public ApiRequestDto(String action, Object dataObject) {
        this.id = "325ege324ll23el42uicc";
        this.action = action;
        this.dataObject = dataObject;
    }
}

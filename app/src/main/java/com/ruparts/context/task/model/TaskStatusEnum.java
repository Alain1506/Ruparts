package com.ruparts.context.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatusEnum {
    @JsonProperty("to_do")
    TO_DO("to_do"),

    @JsonProperty("in_progress")
    IN_PROGRESS("in_progress"),

    @JsonProperty("completed")
    COMPLETED("completed"),

    @JsonProperty("canceled")
    CANCELLED("canceled");

    private String statusFromLibrary;

    TaskStatusEnum(String statusFromLibrary) {
        this.statusFromLibrary = statusFromLibrary;
    }

    public String getStatusFromLibrary() {
        return statusFromLibrary;
    }

    public void setStatusFromLibrary(String statusFromLibrary) {
        this.statusFromLibrary = statusFromLibrary;
    }
}

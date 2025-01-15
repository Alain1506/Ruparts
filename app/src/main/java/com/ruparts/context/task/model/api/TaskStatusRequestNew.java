package com.ruparts.context.task.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStatusRequestNew {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("status")
    public String status;
}

package com.ruparts.context.task.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruparts.context.task.model.TaskObject;

public class TaskStatusRequest {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("status")
    public String status;

    public TaskStatusRequest(TaskObject task) {
        this.id = task.id.id;
        this.status = task.status.getStatusFromLibrary();
    }

}

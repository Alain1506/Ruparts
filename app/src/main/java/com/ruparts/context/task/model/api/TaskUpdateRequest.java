package com.ruparts.context.task.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.main.Defaults;

import java.text.SimpleDateFormat;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskUpdateRequest {

    @JsonUnwrapped
    public TaskId id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("priority")
    public String priority;

    @JsonProperty("finish_at")
    public String finishAt;

    @JsonProperty("implementer")
    public String implementer;

    public TaskUpdateRequest(TaskObject task) {

        id = (task.id.equals(0)) ? null : task.id;
        title = (task.taskTitle == null) ? null : task.taskTitle;
        description = (task.taskDescription == null) ? null : task.taskDescription;
        priority = (task.taskPriority == null) ? null : task.taskPriority;
        implementer = (task.taskImplementer == null) ? null : task.taskImplementer;

        if (task.taskFinishAt == null) {
            finishAt = "";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Defaults.DATE_FORMAT);
            finishAt = simpleDateFormat.format(task.taskFinishAt);
        }
    }
}

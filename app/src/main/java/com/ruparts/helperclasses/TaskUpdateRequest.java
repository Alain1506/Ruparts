package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;

import java.text.SimpleDateFormat;


public class TaskUpdateRequest {

    @JsonProperty("id")
    public String id;
    @JsonProperty("action")
    public String action;
    @JsonProperty("data")
    public TaskUpdateObject dataObject;

    public TaskUpdateRequest(TaskObject task) {
        dataObject = new TaskUpdateObject(task);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TaskUpdateObject {

        @JsonProperty("id")
        public TaskId tuoId;
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

        public TaskUpdateObject(TaskObject task) {

            tuoId = (task.id.equals(0)) ? null : task.id;
            title = (task.taskTitle == null) ? null : task.taskTitle;
            description = (task.taskDescription == null) ? null : task.taskDescription;
            priority = (task.taskPriority == null) ? null : task.taskPriority;
            implementer = (task.taskImplementer == null) ? null : task.taskImplementer;

            if (task.taskFinishAt == null) {
                finishAt = "";
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                finishAt = simpleDateFormat.format(task.taskFinishAt);
            }
        }
    }

}


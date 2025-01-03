package com.example.navigationdrawer.helperclasses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TaskUpdateRequest {

    @JsonProperty("id")
    public String id;
    @JsonProperty("action")
    public String action;
    @JsonProperty("data")
    public TaskUpdateObject dataObject;

    public TaskUpdateRequest(TaskObject tbo) {
        dataObject = new TaskUpdateObject(tbo);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TaskUpdateObject {

        @JsonProperty("id")
        public Integer tuoId;
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

            tuoId = (task.taskId == 0) ? 0 : task.taskId;
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


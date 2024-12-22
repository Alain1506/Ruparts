package com.example.navigationdrawer.helperclasses;

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
    private TaskBodyObject tbo;


    public TaskUpdateRequest(TaskBodyObject tbo) {
        dataObject = new TaskUpdateObject(tbo);
    }
    public class TaskUpdateObject {

        @JsonProperty("id")
        public Integer tuo_id;
        @JsonProperty("title")
        public String title;
        @JsonProperty("description")
        public String description;
        @JsonProperty("priority")
        public String priority;
        @JsonProperty("finish_at")
        public String finish_at;
        @JsonProperty("implementer")
        public String implementer;

        public TaskUpdateObject(TaskBodyObject tbo) {
            tuo_id = tbo.tbdo_id;
            title = tbo.title;
            description = tbo.description;
            priority = tbo.priority;

            Date finishDate = tbo.finish_at;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            finish_at = simpleDateFormat.format(finishDate);

            implementer = tbo.implementer;
        }
    }

}


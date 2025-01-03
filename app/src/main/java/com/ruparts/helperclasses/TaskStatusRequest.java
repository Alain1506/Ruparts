package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TaskStatusRequest {

    @JsonProperty("id")
    public String id;
    @JsonProperty("action")
    public String action;
    @JsonProperty("data")
    public TaskStatusRequestObject dataObject;
//    private TaskBodyObject tbo;


    public TaskStatusRequest(TaskObject tbo) {
        dataObject = new TaskStatusRequestObject();
        dataObject.tsro_id = tbo.taskId;
        dataObject.tsro_status = tbo.taskStatus;
    }
    public class TaskStatusRequestObject {

        @JsonProperty("id")
        public Integer tsro_id;
        @JsonProperty("status")
        public String tsro_status;
    }
}

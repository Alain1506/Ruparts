package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.TaskStatusEnum;

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
        dataObject.tsro_id = tbo.id;
        dataObject.tsro_status = tbo.status;
    }
    public class TaskStatusRequestObject {

        @JsonProperty("id")
        public TaskId tsro_id;
        @JsonProperty("status")
        public TaskStatusEnum tsro_status;
    }
}

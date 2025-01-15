package com.ruparts.context.task.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruparts.context.task.model.TaskStatusEnum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskListRequest {
    @JsonProperty("title")
    public String title;
    @JsonProperty("author_id")
    public String authorId;
    @JsonProperty("status")
    public TaskStatusEnum status;
    @JsonProperty("type")
    public String type;
    @JsonProperty("implementer")
    public String implementer;
    @JsonProperty("finish_date_before")
    public Date finishDateBefore;
    @JsonProperty("finish_date_after")
    public Date finishDateAfter;
    @JsonProperty("_pagination")
    public Map<String, Integer> pagination;
    @JsonProperty("_sorting")
    public Map<String, String> sorting;

    public TaskListRequest() {
        pagination = new HashMap<>();
        pagination.put("page", 1);
        pagination.put("per_page", 10);
        sorting = new HashMap<>();
        sorting.put("field",null);
        sorting.put("direction",null);
    }
}

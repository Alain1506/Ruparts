package com.ruparts.helperclasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TaskObjectRequest {
    @JsonProperty("id")
    public String id;

    @JsonProperty("action")
    public String action;

    @JsonProperty("data")
    public TaskDataObject dataObject;

    public TaskObjectRequest() {
        dataObject = new TaskDataObject();
    }

    public class TaskDataObject {

        @JsonProperty("title")
        public String title;

        @JsonProperty("author_id")
        public String author_id;

        @JsonProperty("status")
        public String status;

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

        public TaskDataObject() {
            pagination = new HashMap<>();
            pagination.put("page", 1);
            pagination.put("per_page", 10);
            sorting = new HashMap<>();
            sorting.put("field",null);
            sorting.put("direction",null);
        }
    }
}


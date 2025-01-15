package com.ruparts.context.task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.ruparts.main.Defaults;


import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskObject implements Serializable {

    public TaskObject() {
        taskBodyDataAuthor = new TaskBodyDataAuthor();
//                listOfHistory = new ArrayList<>();
    }

    @JsonUnwrapped
    public TaskId id;

    @JsonProperty("id")
    public int taskId;

    @JsonProperty("author")
    public TaskBodyDataAuthor taskBodyDataAuthor;

    @JsonProperty("title")
    public String taskTitle;

    @JsonProperty("description")
    public String taskDescription;

    @JsonProperty("type")
    public String taskType;

    @JsonProperty("status")
    public TaskStatusEnum status;

    @JsonProperty("status")
    public String taskStatus;

    @JsonProperty("priority")
    public String taskPriority;

    @JsonProperty("implementer")
    public String taskImplementer;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Defaults.DATE_TIME_FORMAT)
    public Date taskCreatedAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Defaults.DATE_TIME_FORMAT)
    public Date taskUpdatedAt;

    @JsonProperty("finish_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Defaults.DATE_FORMAT)
    public Date taskFinishAt;

    @JsonProperty("created_at_diff")
    public String taskCreatedAtDiff;

    @JsonProperty("attributes")
    @JsonIgnore
    public String[] taskAttributes;                        //какой тип переменной?

    @JsonProperty("files")
    @JsonIgnore
    public String[] taskFiles;                              //какой тип переменной?

    @JsonProperty("related_tasks")
    @JsonIgnore
    public String[] taskRelatedTasks;                           //какой тип переменной?

////            @JsonProperty("history")
//            public ArrayList<TaskBodyDataHistory> listOfHistory;

    public class TaskBodyDataAuthor implements Serializable {

        public TaskBodyDataAuthor() {
        }

        @JsonProperty("uuid")
        public String uuid;

        @JsonProperty("username")
        public String username;

        @JsonProperty("display_name")
        public String displayName;
    }

//            public class TaskBodyDataHistory implements Serializable {
//                public TaskBodyDataHistory(){}
//                @JsonProperty("author")
//                public TaskBodyDataAuthor tbda;
//                @JsonProperty("text")
//                public String text;
//                @JsonProperty("created_at")
//                public Date created_at;
//            }
}


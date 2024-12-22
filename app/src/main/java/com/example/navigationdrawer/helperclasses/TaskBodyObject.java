package com.example.navigationdrawer.helperclasses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public class TaskBodyObject implements Serializable {
            public TaskBodyObject() {
                taskBodyDataAuthor = new TaskBodyDataAuthor();
//                listOfHistory = new ArrayList<>();
            }
            @JsonProperty("id")
            public int tbdo_id;
            @JsonProperty("author")
            public TaskBodyDataAuthor taskBodyDataAuthor;
            @JsonProperty("title")
            public String title;
            @JsonProperty("description")
            public String description;
            @JsonProperty("type")
            public String type;
            @JsonProperty("status")
            public String status;
            @JsonProperty("priority")
            public String priority;
            @JsonProperty("implementer")
            public String implementer;
            @JsonProperty("created_at")
            @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT")
            public Date created_at;
            @JsonProperty("updated_at")
            @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT")
            public Date updated_at;
            @JsonProperty("finish_at")
            public Date finish_at;
            @JsonProperty("created_at_diff")
            public String created_at_diff;
            @JsonProperty("attributes")
            @JsonIgnore
            public String[] attributes;                             //какой тип переменной?
            @JsonProperty("files")
            @JsonIgnore
            public String[] tbdo_files;                              //какой тип переменной?
            @JsonProperty("related_tasks")
            @JsonIgnore
            public String[] related_tasks;                           //какой тип переменной?
////            @JsonProperty("history")
//            public ArrayList<TaskBodyDataHistory> listOfHistory;

            public class TaskBodyDataAuthor implements Serializable {
                public TaskBodyDataAuthor(){}
                @JsonProperty("uuid")
                public String uuid;
                @JsonProperty("username")
                public String username;
                @JsonProperty("display_name")
                public String display_name;
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


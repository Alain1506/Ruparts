package com.example.navigationdrawer.helperclasses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
            public String created_at;
            @JsonProperty("updated_at")
            public String updated_at;
            @JsonProperty("finish_at")
            public String finish_at;
            @JsonProperty("created_at_diff")
            public String created_at_diff;
            @JsonProperty("attributes")
            public String[] attributes;                             //какой тип переменной?
            @JsonProperty("files")
            public String[] tbdo_files;                              //какой тип переменной?
            @JsonProperty("related_tasks")
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


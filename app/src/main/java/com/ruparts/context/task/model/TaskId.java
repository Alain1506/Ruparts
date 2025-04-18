package com.ruparts.context.task.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public  class TaskId implements Serializable {

    @JsonProperty("id")
    public int id;

    public TaskId(@JsonProperty("id") int id) {
        this.id = id;
    }

    public static TaskId fromInt(int id) {
        return new TaskId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return id == taskId.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

package com.ruparts.context.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public final class TaskId {
    @JsonProperty("id")
    public final int id;

    public TaskId(int id) {
        this.id = id;
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

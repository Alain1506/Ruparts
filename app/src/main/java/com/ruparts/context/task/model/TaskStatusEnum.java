package com.ruparts.context.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskStatusEnum {
    @JsonProperty("to_do")
    TO_DO,

    @JsonProperty("in_progress")
    IN_PROGRESS,

    @JsonProperty("completed")
    COMPLETED,

    @JsonProperty("cancelled")
    CANCELLED;
}

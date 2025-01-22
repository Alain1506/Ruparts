package com.ruparts.context.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.api.TaskListRequest;
import com.ruparts.context.task.model.api.TaskStatusRequestNew;
import com.ruparts.context.task.model.api.TaskUpdateRequestNew;
import com.ruparts.main.ApiClient;
import com.ruparts.main.exception.api.DecodeException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TaskApiClient {

    private ApiClient client;

    public TaskApiClient(ApiClient client) {
        this.client = client;
    }

    public TaskObject read(TaskId taskId) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.read", taskId);

        try {
            TaskObject task = this.client.getObjectMapper().readValue(responseData.toString(), TaskObject.class);

            return task;
        } catch (JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

    public List<TaskObject> list(TaskListRequest taskListRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.list", taskListRequest);

        try {
            String jsonListOfTasks = responseData.getJSONArray("list").toString();

            List<TaskObject> listOfTasks = this.client.getObjectMapper().readValue(
                jsonListOfTasks,
                TypeFactory.defaultInstance().constructCollectionType(List.class, TaskObject.class)
            );

            return listOfTasks;
        } catch (JSONException | JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

    public TaskObject update(TaskUpdateRequestNew taskUpdateRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.update", taskUpdateRequest);

        try {
            TaskObject task = this.client.getObjectMapper().readValue(responseData.toString(), TaskObject.class);

            return task;
        } catch (JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

    public TaskObject updateStatus(TaskStatusRequestNew taskStatusRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.status", taskStatusRequest);

        try {
            TaskObject task = this.client.getObjectMapper().readValue(responseData.toString(), TaskObject.class);

            return task;
        } catch (JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

}

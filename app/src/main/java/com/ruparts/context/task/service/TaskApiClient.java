package com.ruparts.context.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.api.TaskListRequest;
import com.ruparts.context.task.model.api.TaskStatusRequest;
import com.ruparts.context.task.model.api.TaskUpdateRequest;
import com.ruparts.context.library.TaskLibraryModel;
import com.ruparts.context.task.model.api.LibraryRequest;
import com.ruparts.main.ApiClient;
import com.ruparts.main.exception.api.DecodeException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    public TaskObject update(TaskUpdateRequest taskUpdateRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.update", taskUpdateRequest);

        try {
            TaskObject task = this.client.getObjectMapper().readValue(responseData.toString(), TaskObject.class);

            return task;
        } catch (JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

    public TaskObject updateStatus(TaskStatusRequest taskStatusRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.status", taskStatusRequest);

        try {
            TaskObject task = this.client.getObjectMapper().readValue(responseData.toString(), TaskObject.class);

            return task;
        } catch (JsonProcessingException e) {
            throw new DecodeException(e);
        }
    }

    public TaskLibraryModel getLibrary(LibraryRequest libraryRequest) {
        JSONObject responseData = this.client.callEndpointAndReturnJsonObject("app.task.library", libraryRequest);

        TaskLibraryModel taskLibraryModel = new TaskLibraryModel();
        try {
            String task_types = responseData.getJSONObject("task_types").toString();
            taskLibraryModel.taskTypes = this.client.getObjectMapper().readValue(task_types, HashMap.class);
            String user_roles = responseData.getJSONObject("user_roles").toString();
            taskLibraryModel.userRoles = this.client.getObjectMapper().readValue(user_roles, HashMap.class);
            String user_roles_editable = responseData.getJSONObject("user_roles_editable").toString();
            taskLibraryModel.userRolesEditable = this.client.getObjectMapper().readValue(user_roles_editable, HashMap.class);
            String implementer = responseData.getJSONObject("implementer").toString();
            taskLibraryModel.implementer = this.client.getObjectMapper().readValue(implementer, HashMap.class);
            String status = responseData.getJSONObject("status").toString();
            taskLibraryModel.status = this.client.getObjectMapper().readValue(status, HashMap.class);
            String id_reference_type = responseData.getJSONObject("id_reference_type").toString();
            taskLibraryModel.idReferenceType = this.client.getObjectMapper().readValue(id_reference_type, HashMap.class);

            return taskLibraryModel;
        } catch (JsonProcessingException | JSONException e) {
            throw new DecodeException(e);
        }

    }

}

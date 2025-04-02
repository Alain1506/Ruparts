package com.ruparts.context.task.service;

import com.ruparts.context.task.model.TaskFilter;
import com.ruparts.context.task.model.TaskId;
import com.ruparts.context.task.model.TaskObject;
import com.ruparts.context.task.model.api.TaskListRequest;
import com.ruparts.context.task.model.api.TaskStatusRequest;
import com.ruparts.context.task.model.api.TaskUpdateRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    private Map<TaskId, TaskObject> taskObjectsCache;
    private TaskApiClient apiClient;

    public TaskRepository(TaskApiClient apiClient) {
        this.taskObjectsCache = new HashMap<>();
        this.apiClient = apiClient;
    }

    public TaskObject getById(TaskId taskId) {
        // вызов АПИ получения одной задачи (task)
        if (this.taskObjectsCache.containsKey(taskId)) {
            return this.taskObjectsCache.get(taskId);
        }
        TaskObject task = this.apiClient.read(taskId);
        this.taskObjectsCache.put(task.id, task);

        return task;
    }

    public List<TaskObject> getByFilter(TaskFilter taskFilter) {
        // вызов АПИ получения списка (list)
        TaskListRequest taskListRequest = new TaskListRequest();
        taskListRequest.filter.search = taskFilter.search;
        taskListRequest.filter.status = taskFilter.status;
        List<TaskObject> tasks = this.apiClient.list(taskListRequest);

        for (TaskObject task: tasks) {
            this.taskObjectsCache.put(task.id, task);
        }

        return tasks;
    }

    public TaskObject saveTask(TaskObject task) {
        // вызов АПИ сохранения
        task = this.apiClient.update(new TaskUpdateRequest(task));
        this.taskObjectsCache.put(task.id, task);

        return task;
    }

    public TaskObject changeTaskStatus(TaskObject task) {
        // вызов АПИ смены статуса
        task = this.apiClient.updateStatus(new TaskStatusRequest(task));
        this.taskObjectsCache.put(task.id, task);

        return task;
    }

//    public TaskLibraryModel getLibrary() {
//        taskLibraryModel = this.apiClient.getLibrary(new LibraryRequest());
//        return taskLibraryModel;
//    }

    public void reset() {
        // очистить кеш
        this.taskObjectsCache = new HashMap<>();
    }
}

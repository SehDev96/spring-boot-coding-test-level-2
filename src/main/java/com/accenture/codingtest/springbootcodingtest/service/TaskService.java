package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Task;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TaskService {

    List<Task> getAllTask();

    List<Task> getAllActiveTasks();

    List<Task> getAllCompletedTasks();

    List<Task> getTaskByUserId(UUID user_id);

    List<Task> getTaskByProject(UUID project_id);

    Task getTaskById(UUID id);

    Task createTask(Task task);

    Task updateTask(Task task);

    Map<String,String> patchTask(Map<String,String> fields);

    void deleteTask(UUID id);
}

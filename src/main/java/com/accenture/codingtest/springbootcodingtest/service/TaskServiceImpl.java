package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.constants.TaskStatus;
import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceNotFoundException;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAllActiveTasks() {
        return taskRepository.findAllActiveTasks();
    }

    @Override
    public List<Task> getAllCompletedTasks() {
        return taskRepository.findAllCompletedTasks();
    }

    @Override
    public List<Task> getTaskByUserId(UUID user_id) {
        return taskRepository.findAllByUser_id(user_id);
    }

    @Override
    public List<Task> getTaskByProject(UUID project_id) {
        return taskRepository.findAllByProject_id(project_id);
    }

    @Override
    public Task getTaskById(UUID id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) throw new ResourceNotFoundException("Task", "id", id.toString());
        return task;
    }

    @Override
    public Task createTask(Task task) {
        task.setStatus(TaskStatus.NOT_STARTED.toString());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        Task dbTask = taskRepository.findById(task.getId()).orElse(null);
        if (dbTask == null) throw new ResourceNotFoundException("Task", "id", task.getId().toString());
        return taskRepository.save(task);
    }

    @Override
    public Map<String, String> patchTaskUser(Map<String, String> fields) {
        Task task = taskRepository.findById(UUID.fromString(fields.get("id"))).orElse(null);
        if (task == null) throw new ResourceNotFoundException("Task", "id", fields.get("id"));

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            switch (entry.getKey()) {
                case "title":
                    task.setTitle(fields.get("title"));
                    break;
                case "description":
                    task.setDescription(fields.get("description"));
                    break;
                case "status":
                    task.setStatus(fields.get("status"));
                    break;
            }
        }

        taskRepository.save(task);
        return fields;
    }

    @Override
    public Map<String, String> patchTaskProductOwner(Map<String, String> fields) {
        Task task = taskRepository.findById(UUID.fromString(fields.get("id"))).orElse(null);
        if (task == null) throw new ResourceNotFoundException("Task", "id", fields.get("id"));

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            switch (entry.getKey()) {
                case "title":
                    task.setTitle(fields.get("title"));
                    break;
                case "description":
                    task.setDescription(fields.get("description"));
                    break;
                case "status":
                    task.setStatus(fields.get("status"));
                    break;
                case "user_id":
                    task.setUser_id(UUID.fromString(fields.get("user_id")));
                    break;
                case "project_id":
                    task.setProject_id(UUID.fromString(fields.get("project_id")));
                    break;
            }
        }

        taskRepository.save(task);
        return fields;
    }

    @Override
    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task == null) throw new ResourceNotFoundException("Task", "id", id.toString());
        taskRepository.deleteById(id);
    }
}

package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.model.ApiResponse;
import com.accenture.codingtest.springbootcodingtest.service.TaskService;
import com.accenture.codingtest.springbootcodingtest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/tasks")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        List<Task> taskLists = taskService.getAllTask();
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Tasks successfully retrieved",
                taskLists
        ), HttpStatus.OK);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<?> getTasksById(@PathVariable String task_id) {
        Task task = taskService.getTaskById(UUID.fromString(task_id));
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Task successfully retrieved",
                task
        ), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task, Principal principal) {
        // if user id is null it will be assign to the product owner by default
        if(task.getUser_id() == null){
            UUID user_id = userService.getUserIdByUsername(principal.getName());
            if(user_id!=null) task.setUser_id(user_id);
        }
        Task dbTask = taskService.createTask(task);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.CREATED.value(),
                "Task has been successfully created",
                dbTask
        ), HttpStatus.CREATED);
    }

    @PatchMapping("/{task_id}")
    public ResponseEntity<?> patchTask(@PathVariable String task_id, @RequestBody  Map<String, String> fields) {
        if (task_id.isBlank() || task_id.isEmpty()) throw new RuntimeException();
        fields.put("id", task_id);
        Map<String, String> updatedFields = taskService.patchTaskUser(fields);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Task successfully updated!",
                updatedFields
        ), HttpStatus.OK);
    }

    @PatchMapping("/assign/{task_id}")
    public ResponseEntity<?> assignTask(@PathVariable String task_id,@RequestBody Map<String,String> fields) {
        // request body will be {"user_id":<UUID>}
        if (task_id.isBlank() || task_id.isEmpty()) throw new RuntimeException();
        fields.put("id",task_id);
        Map<String,String> updatedFields = taskService.patchTaskProductOwner(fields);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Task successfully updated",
                updatedFields
        ),HttpStatus.OK);
    }


}

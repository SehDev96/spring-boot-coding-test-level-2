package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.model.ApiResponse;
import com.accenture.codingtest.springbootcodingtest.service.TaskService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/tasks")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks(){
        List<Task> taskLists = taskService.getAllTask();
        return new ResponseEntity<>( new ApiResponse(
                HttpStatus.OK.value(),
                "Tasks successfully retrieved",
                taskLists
        ), HttpStatus.OK);
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<?> getTasksById(@PathVariable String task_id){
        Task task = taskService.getTaskById(UUID.fromString(task_id));
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Task successfully retrieved",
                task
        ),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task){
        Task dbTask = taskService.createTask(task);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.CREATED.value(),
                "Task has been successfully created",
                dbTask
        ),HttpStatus.CREATED);
    }

    @PatchMapping("/{task_id}")
    public ResponseEntity<?> patchTask(Map<String,String> fields){
        Map<String,String> updatedFields = taskService.patchTask(fields);
        return new ResponseEntity<>( new ApiResponse(
                HttpStatus.OK.value(),
                "Task successfully updated!",
                updatedFields
        ),HttpStatus.OK);
    }
}

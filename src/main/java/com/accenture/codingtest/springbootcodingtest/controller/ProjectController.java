package com.accenture.codingtest.springbootcodingtest.controller;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.model.ApiResponse;
import com.accenture.codingtest.springbootcodingtest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/api/v1/projects")
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        List<Project> projectList = projectService.getAllProjects();
        return new ResponseEntity<>( new ApiResponse(
                HttpStatus.OK.value(),
                "Successfully retrieved all the projects",
                projectList
        ), HttpStatus.OK);
    }

    @GetMapping("/{project_id}")
    public ResponseEntity<?> getProjectById(@PathVariable String project_id){
        Project project = projectService.getProjectById(UUID.fromString(project_id));
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Project sucessfully retrieved",
                project
        ),HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project){
        Project dbProject = projectService.createProject(project);
        return new ResponseEntity<>( new ApiResponse(
                HttpStatus.CREATED.value(),
                "Project successfully created!",
                dbProject
        ),HttpStatus.CREATED);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity<?> updateProject(@RequestBody Project project){
        Project dbProject = projectService.updateProject(project);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Project successfully updated",
                dbProject
        ),HttpStatus.OK);
    }

    @PatchMapping("/{project_id}")
    public ResponseEntity<?> patchProject(@RequestBody Map<String,String> fields){
        Map<String,String> updatedFields = projectService.patchProject(fields);
        return new ResponseEntity<>(new ApiResponse(
                HttpStatus.OK.value(),
                "Project fields has been successfully updated",
                updatedFields
        ), HttpStatus.OK);
    }
}

package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProjectService {

    List<Project> getAllProjects();

    Project getProjectById(UUID id);

    Project createProject(Project project);

    Project updateProject(Project project);

    Map<String,String> patchProject(Map<String,String> fields);

    void deleteProject(UUID project_id);
}

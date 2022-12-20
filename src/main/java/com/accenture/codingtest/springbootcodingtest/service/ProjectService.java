package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProjectService {

    List<Project> getAllProjects(String q,
                                 int pageIndex,
                                 int pageSize,
                                 String sortBy,
                                 String sortDirection);

    Project getProjectById(UUID id);

    Project createProject(Project project);

    Project updateProject(Project project);

    Map<String,String> patchProject(Map<String,String> fields);

    void deleteProject(UUID project_id);
}

package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceExistException;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceNotFoundException;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(UUID id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) throw new ResourceNotFoundException("Project", "id", id.toString());
        return project;
    }

    @Override
    public Project createProject(Project project) {
        boolean exists = projectRepository.existsByName(project.getName());
        if (exists) throw new ResourceExistException("Project name", project.getName());
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        Project dbProject = projectRepository.findById(project.getId()).orElse(null);
        if (dbProject == null) throw new ResourceNotFoundException("Project", "id", project.getId().toString());
        return projectRepository.save(project);
    }

    @Override
    public Map<String, String> patchProject(Map<String, String> fields) {

        Project project = projectRepository.findById(UUID.fromString(fields.get("id"))).orElse(null);

        if (project == null) throw new ResourceNotFoundException("Project", "id", fields.get("id"));

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue().equals("name")) project.setName(fields.get("name"));
        }

        projectRepository.save(project);

        fields.remove("id");
        return fields;
    }

    @Override
    public void deleteProject(UUID project_id) {
        Project project = projectRepository.findById(project_id).orElse(null);
        if (project == null) throw new ResourceNotFoundException("Project", "id", project_id.toString());
        projectRepository.deleteById(project_id);
    }
}

package com.accenture.codingtest.springbootcodingtest.service;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceExistException;
import com.accenture.codingtest.springbootcodingtest.exceptions.ResourceNotFoundException;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects(String q,
                                        int pageIndex,
                                        int pageSize,
                                        String sortBy,
                                        String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

        Page<Project> projectPage = null;

        if (q == null) {
            projectPage = projectRepository.findAll(pageable);
        } else {
            projectPage = projectRepository.findAllByNameContaining(q, pageable);
        }

        assert projectPage != null;
        return projectPage.getContent();
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
        project.setCreatedDate(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        Project dbProject = projectRepository.findById(project.getId()).orElse(null);
        if (dbProject == null) throw new ResourceNotFoundException("Project", "id", project.getId().toString());
        project.setUpdatedDate(LocalDateTime.now());
        return projectRepository.save(project);
    }

    @Override
    public Map<String, String> patchProject(Map<String, String> fields) {

        Project project = projectRepository.findById(UUID.fromString(fields.get("id"))).orElse(null);

        if (project == null) throw new ResourceNotFoundException("Project", "id", fields.get("id"));

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getValue().equals("name")) project.setName(fields.get("name"));
        }
        project.setUpdatedDate(LocalDateTime.now());
        projectRepository.save(project);

        fields.put("updated_date", project.getUpdatedDate().toString());
        return fields;
    }

    @Override
    public void deleteProject(UUID project_id) {
        Project project = projectRepository.findById(project_id).orElse(null);
        if (project == null) throw new ResourceNotFoundException("Project", "id", project_id.toString());
        projectRepository.deleteById(project_id);
    }
}

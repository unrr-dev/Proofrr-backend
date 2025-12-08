package com.proofrr.proofrr.service;

import com.proofrr.proofrr.dto.ProjectRequest;
import com.proofrr.proofrr.model.Project;
import com.proofrr.proofrr.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private static final DateTimeFormatter MM_DD_YY = DateTimeFormatter.ofPattern("MM/dd/yy");

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project create(ProjectRequest request) {
        Project project = new Project();
        project.setUserId(request.getUserId());
        project.setShareUuid(generateShareUuid());
        project.setConcept(request.getConcept());
        project.setPlatforms(request.getPlatforms());
        project.setAssetUrls(new ArrayList<>(request.getAssetUrls()));
        project.setReviewer(request.getReviewer());
        project.setPriority(request.getPriority());
        project.setStatus(request.getStatus());
        project.setNotes(request.getNotes());
        project.setDueDate(parseDate(request.getDueDate()));
        return projectRepository.save(project);
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public List<Project> findByUser(Long userId) {
        return projectRepository.findByUserId(userId);
    }

    private LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, MM_DD_YY);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid dueDate. Use MM/dd/yy (e.g., 11/22/24)");
        }
    }

    private String generateShareUuid() {
        String candidate;
        do {
            candidate = UUID.randomUUID().toString();
        } while (projectRepository.existsByShareUuid(candidate));
        return candidate;
    }

    public Project getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("projectId is required");
        }

        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public boolean isValidShareUuid(String shareUuid) {
        if (!StringUtils.hasText(shareUuid)) {
            return false;
        }
        return projectRepository.existsByShareUuid(shareUuid);
    }

    public Project findByShareUuid(String shareUuid) {
        if (!StringUtils.hasText(shareUuid)) {
            throw new IllegalArgumentException("shareUuid is required");
        }

        return projectRepository.findByShareUuid(shareUuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid shareUuid"));
    }
}

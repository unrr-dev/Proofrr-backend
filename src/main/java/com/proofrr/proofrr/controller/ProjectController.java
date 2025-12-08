package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.ProjectRequest;
import com.proofrr.proofrr.dto.ProjectResponse;
import com.proofrr.proofrr.dto.ShareLinkResponse;
import com.proofrr.proofrr.dto.ShareValidationResponse;
import com.proofrr.proofrr.model.Project;
import com.proofrr.proofrr.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final String frontendUrl;

    public ProjectController(ProjectService projectService,
                             @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.projectService = projectService;
        this.frontendUrl = trimTrailingSlash(frontendUrl);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        Project saved = projectService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> all(@RequestParam(value = "userId", required = false) Long userId) {
        List<Project> results = (userId != null)
                ? projectService.findByUser(userId)
                : projectService.findAll();

        List<ProjectResponse> projects = results
                .stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{projectId}/share")
    public ResponseEntity<ShareLinkResponse> getShareLink(@PathVariable("projectId") Long projectId) {
        Project project = projectService.getById(projectId);
        String shareUuid = project.getShareUuid();
        String shareUrl = buildShareUrl(shareUuid);
        return ResponseEntity.ok(new ShareLinkResponse(shareUuid, shareUrl));
    }

    @GetMapping("/share/verify")
    public ResponseEntity<ShareValidationResponse> verifyShare(@RequestParam("shareUuid") String shareUuid) {
        boolean valid = projectService.isValidShareUuid(shareUuid);
        return ResponseEntity.ok(new ShareValidationResponse(valid, shareUuid));
    }

    @GetMapping("/share/{shareUuid}")
    public ResponseEntity<ProjectResponse> getSharedProject(@PathVariable("shareUuid") String shareUuid) {
        Project project = projectService.findByShareUuid(shareUuid);
        return ResponseEntity.ok(ProjectResponse.from(project));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    private String buildShareUrl(String shareUuid) {
        return frontendUrl + "/share/" + shareUuid;
    }

    private String trimTrailingSlash(String url) {
        if (url != null && url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }
}

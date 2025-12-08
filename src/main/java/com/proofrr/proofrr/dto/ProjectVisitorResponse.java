package com.proofrr.proofrr.dto;

import com.proofrr.proofrr.model.ProjectVisitor;

import java.time.Instant;

public class ProjectVisitorResponse {

    private Long id;
    private Long projectId;
    private String displayName;
    private Instant createdAt;
    private Instant lastSeenAt;

    public static ProjectVisitorResponse from(ProjectVisitor visitor) {
        ProjectVisitorResponse response = new ProjectVisitorResponse();
        response.setId(visitor.getId());
        response.setProjectId(visitor.getProject().getId());
        response.setDisplayName(visitor.getDisplayName());
        response.setCreatedAt(visitor.getCreatedAt());
        response.setLastSeenAt(visitor.getLastSeenAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Instant lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }
}

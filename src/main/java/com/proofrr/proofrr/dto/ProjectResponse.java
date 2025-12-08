package com.proofrr.proofrr.dto;

import com.proofrr.proofrr.model.Project;

import java.time.LocalDate;
import java.util.List;

public class ProjectResponse {
    private Long id;
    private Long userId;
    private String shareUuid;
    private String concept;
    private List<String> platforms;
    private List<String> assetUrls;
    private String reviewer;
    private String priority;
    private LocalDate dueDate;
    private String notes;
    private String status;

    public static ProjectResponse from(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setUserId(project.getUserId());
        response.setShareUuid(project.getShareUuid());
        response.setConcept(project.getConcept());
        response.setPlatforms(project.getPlatforms());
        response.setAssetUrls(project.getAssetUrls());
        response.setReviewer(project.getReviewer());
        response.setPriority(project.getPriority());
        response.setDueDate(project.getDueDate());
        response.setNotes(project.getNotes());
        response.setStatus(project.getStatus());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getShareUuid() {
        return shareUuid;
    }

    public void setShareUuid(String shareUuid) {
        this.shareUuid = shareUuid;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public List<String> getAssetUrls() {
        return assetUrls;
    }

    public void setAssetUrls(List<String> assetUrls) {
        this.assetUrls = assetUrls;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

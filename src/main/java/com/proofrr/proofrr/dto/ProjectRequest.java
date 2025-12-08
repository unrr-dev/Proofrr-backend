package com.proofrr.proofrr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProjectRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String concept;

    @NotEmpty
    private List<@NotBlank String> platforms;

    // Cloudinary (or similar) URLs sent by the frontend after upload
    private List<@NotBlank String> assetUrls = new ArrayList<>();

    @NotBlank
    private String reviewer;

    @NotBlank
    private String priority;

    // Accepts MM/dd/yy (e.g., 11/22/24)
    @NotBlank
    private String dueDate;

    private String notes;

    @NotBlank
    private String status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
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

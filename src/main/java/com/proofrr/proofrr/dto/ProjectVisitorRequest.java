package com.proofrr.proofrr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectVisitorRequest {

    @NotBlank(message = "displayName is required")
    @Size(max = 120, message = "displayName cannot exceed 120 characters")
    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

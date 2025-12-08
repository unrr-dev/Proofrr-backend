package com.proofrr.proofrr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NoteRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotBlank(message = "content is required")
    @Size(max = 2000, message = "content cannot exceed 2000 characters")
    private String content;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

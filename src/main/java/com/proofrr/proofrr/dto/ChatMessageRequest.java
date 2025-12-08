package com.proofrr.proofrr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChatMessageRequest {

    @NotNull(message = "visitorId is required")
    private Long visitorId;

    @Size(max = 120, message = "senderName cannot exceed 120 characters")
    private String senderName;

    @NotBlank(message = "content is required")
    @Size(max = 2000, message = "content cannot exceed 2000 characters")
    private String content;

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

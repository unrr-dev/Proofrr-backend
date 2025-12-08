package com.proofrr.proofrr.dto;

import com.proofrr.proofrr.model.ProjectChatMessage;

import java.time.Instant;

public class ChatMessageResponse {

    private Long id;
    private Long projectId;
    private Long visitorId;
    private Long userId;
    private String senderName;
    private String content;
    private Instant createdAt;

    public static ChatMessageResponse from(ProjectChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setProjectId(message.getProject().getId());
        response.setVisitorId(message.getVisitor() != null ? message.getVisitor().getId() : null);
        response.setUserId(message.getUserId());
        response.setSenderName(message.getSenderName());
        response.setContent(message.getContent());
        response.setCreatedAt(message.getCreatedAt());
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

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

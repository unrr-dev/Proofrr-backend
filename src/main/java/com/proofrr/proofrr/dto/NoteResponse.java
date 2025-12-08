package com.proofrr.proofrr.dto;

import com.proofrr.proofrr.model.Note;

import java.time.Instant;

public class NoteResponse {

    private Long id;
    private Long userId;
    private String content;
    private Instant createdAt;

    public NoteResponse() {
    }

    public NoteResponse(Long id, Long userId, String content, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static NoteResponse from(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getUserId(),
                note.getContent(),
                note.getCreatedAt()
        );
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

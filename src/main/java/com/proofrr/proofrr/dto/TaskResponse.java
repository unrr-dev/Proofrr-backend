package com.proofrr.proofrr.dto;

import com.proofrr.proofrr.category.Category;
import com.proofrr.proofrr.model.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.EntityNotFoundException;

public class TaskResponse {
    private Long id;
    private Long userId;
    private String title;
    private Long categoryId;
    private String categoryName;
    private LocalDate scheduledDate;
    private LocalTime scheduledTime;
    private boolean completed;

    public static TaskResponse from(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setUserId(task.getUserId());
        response.setTitle(task.getTitle());

        try {
            Category category = task.getCategory();
            if (category != null) {
                response.setCategoryId(category.getId());
                response.setCategoryName(category.getName());
            }
        } catch (EntityNotFoundException ex) {
            // Category row missing; leave category fields null to avoid breaking responses.
        }

        response.setScheduledDate(task.getScheduledDate());
        response.setScheduledTime(task.getScheduledTime());
        response.setCompleted(task.isCompleted());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}

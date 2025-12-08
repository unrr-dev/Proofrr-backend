package com.proofrr.proofrr.dto;

import java.util.Map;

public class TaskSummaryResponse {
    private Long userId;
    private long total;
    private long completed;
    private long pending;
    private Map<String, Long> pendingByCategory;

    public TaskSummaryResponse() {
    }

    public TaskSummaryResponse(Long userId, long total, long completed, long pending, Map<String, Long> pendingByCategory) {
        this.userId = userId;
        this.total = total;
        this.completed = completed;
        this.pending = pending;
        this.pendingByCategory = pendingByCategory;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCompleted() {
        return completed;
    }

    public void setCompleted(long completed) {
        this.completed = completed;
    }

    public long getPending() {
        return pending;
    }

    public void setPending(long pending) {
        this.pending = pending;
    }

    public Map<String, Long> getPendingByCategory() {
        return pendingByCategory;
    }

    public void setPendingByCategory(Map<String, Long> pendingByCategory) {
        this.pendingByCategory = pendingByCategory;
    }
}

package com.proofrr.proofrr.category;

public class CategoryResponse {

    private Long id;
    private String name;
    private Long userId;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, Long userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}

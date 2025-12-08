package com.proofrr.proofrr.service;

import com.proofrr.proofrr.category.Category;
import com.proofrr.proofrr.category.CategoryRepository;
import com.proofrr.proofrr.dto.TaskRequest;
import com.proofrr.proofrr.dto.TaskSummaryResponse;
import com.proofrr.proofrr.model.Task;
import com.proofrr.proofrr.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    public Task create(TaskRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Task task = new Task();
        task.setUserId(request.getUserId());
        task.setTitle(request.getTitle());
        task.setCategory(category);
        task.setCompleted(request.isCompleted());
        task.setScheduledDate(parseDate(request.getScheduledDate()));
        task.setScheduledTime(parseTime(request.getScheduledTime()));
        return taskRepository.save(task);
    }

    public List<Task> findByUser(Long userId, Long categoryId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        if (categoryId != null) {
            return taskRepository.findByUserIdAndCategory_Id(userId, categoryId);
        }
        return taskRepository.findByUserId(userId);
    }

    public TaskSummaryResponse summary(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        List<Task> tasks = taskRepository.findByUserId(userId);
        long total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        long pending = total - completed;

        Map<String, Long> pendingByCategory = tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.groupingBy(task -> task.getCategory().getName(), Collectors.counting()));

        return new TaskSummaryResponse(userId, total, completed, pending, pendingByCategory);
    }

    private LocalDate parseDate(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return LocalDate.parse(value, DATE_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid scheduledDate. Use MM/dd/yy (e.g., 11/22/24)");
        }
    }

    private LocalTime parseTime(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        try {
            return LocalTime.parse(value, TIME_FORMATTER);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid scheduledTime. Use hh:mm a (e.g., 07:00 AM)");
        }
    }
}

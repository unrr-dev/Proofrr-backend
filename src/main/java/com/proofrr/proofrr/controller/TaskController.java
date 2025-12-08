package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.TaskRequest;
import com.proofrr.proofrr.dto.TaskResponse;
import com.proofrr.proofrr.dto.TaskSummaryResponse;
import com.proofrr.proofrr.model.Task;
import com.proofrr.proofrr.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        Task saved = taskService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> all(@RequestParam("userId") Long userId,
                                                  @RequestParam(value = "categoryId", required = false) Long categoryId) {
        List<Task> results = taskService.findByUser(userId, categoryId);
        List<TaskResponse> tasks = results
                .stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/summary")
    public ResponseEntity<TaskSummaryResponse> summary(@RequestParam("userId") Long userId) {
        return ResponseEntity.ok(taskService.summary(userId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

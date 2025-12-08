package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.ChatMessageResponse;
import com.proofrr.proofrr.dto.ProjectVisitorRequest;
import com.proofrr.proofrr.dto.ProjectVisitorResponse;
import com.proofrr.proofrr.model.ProjectChatMessage;
import com.proofrr.proofrr.model.ProjectVisitor;
import com.proofrr.proofrr.service.ProjectChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ProjectChatController {

    private final ProjectChatService chatService;

    public ProjectChatController(ProjectChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/projects/share/{shareUuid}/visitors")
    public ResponseEntity<ProjectVisitorResponse> registerVisitor(@PathVariable("shareUuid") String shareUuid,
                                                                  @Valid @RequestBody ProjectVisitorRequest request) {
        ProjectVisitor visitor = chatService.registerVisitor(shareUuid, request.getDisplayName());
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectVisitorResponse.from(visitor));
    }

    @GetMapping("/projects/share/{shareUuid}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesForShare(@PathVariable("shareUuid") String shareUuid) {
        List<ChatMessageResponse> messages = chatService.getMessagesByShareUuid(shareUuid)
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/projects/{projectId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesForProject(@PathVariable("projectId") Long projectId) {
        List<ChatMessageResponse> messages = chatService.getMessagesByProjectId(projectId)
                .stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

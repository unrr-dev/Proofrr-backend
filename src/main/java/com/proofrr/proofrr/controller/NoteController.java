package com.proofrr.proofrr.controller;

import com.proofrr.proofrr.dto.NoteRequest;
import com.proofrr.proofrr.dto.NoteResponse;
import com.proofrr.proofrr.model.Note;
import com.proofrr.proofrr.service.NoteService;
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
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public ResponseEntity<NoteResponse> create(@Valid @RequestBody NoteRequest request) {
        Note saved = noteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(NoteResponse.from(saved));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> byUser(@RequestParam("userId") Long userId) {
        List<NoteResponse> responses = noteService.findByUser(userId)
                .stream()
                .map(NoteResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

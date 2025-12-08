package com.proofrr.proofrr.service;

import com.proofrr.proofrr.dto.NoteRequest;
import com.proofrr.proofrr.model.Note;
import com.proofrr.proofrr.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note create(NoteRequest request) {
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }

        Note note = new Note();
        note.setUserId(request.getUserId());
        note.setContent(request.getContent());
        return noteRepository.save(note);
    }

    public List<Note> findByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return noteRepository.findByUserId(userId);
    }
}

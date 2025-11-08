package com.logai.backend.controller;

import com.logai.backend.model.Note;
import com.logai.backend.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public ResponseEntity<List<Note>> getAllNotes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email extracted from JWT
        List<Note> notes = noteRepository.findByUserId(email);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        note.setUserId(email);
        Note savedNote = noteRepository.save(note);
        return ResponseEntity.ok(savedNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, @RequestBody Note updatedNote) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return noteRepository.findById(id)
                .filter(note -> note.getUserId().equals(email)) // user can only edit their own note
                .map(existingNote -> {
                    existingNote.setTitle(updatedNote.getTitle());
                    existingNote.setDescription(updatedNote.getDescription());
                    existingNote.touch(); // refresh updatedAt
                    Note saved = noteRepository.save(existingNote);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.status(403).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return noteRepository.findById(id)
                .filter(note -> note.getUserId().equals(email)) // user can only delete their own note
                .map(note -> {
                    noteRepository.delete(note);
                    return ResponseEntity.ok("Note deleted successfully");
                })
                .orElse(ResponseEntity.status(403).body("Not authorized to delete this note"));
    }
}
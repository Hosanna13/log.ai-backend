package com.logai.backend.service;

import com.logai.backend.model.Note;
import com.logai.backend.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService{
    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    // GET commantds 
    public List<Note> getALlNotes() {
        return repo.findAll();
    }
    public List<Note> getNotesByUserId(String userId) {
        return repo.findByUserId(userId);
    }
    // POST commands

    public Note createNote(Note note) {
        return repo.save(note);
    }

    // UPDATE
    public Note updateNote(String id, Note updatedNote) {
        Note note = repo.findById(id).orElseThrow();
        note.setTitle(updatedNote.getTitle());
        note.setDescription(updatedNote.getDescription());
        note.setUpdatedAt(java.time.LocalDateTime.now());
        return repo.save(note);
    }

    public void deleteNote(String id) {
        repo.deleteById(id);
    }
}
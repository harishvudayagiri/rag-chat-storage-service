package com.rag.chatbot.service;

import com.rag.chatbot.dto.CreateSessionRequest;
import com.rag.chatbot.dto.RenameSessionRequest;
import com.rag.chatbot.exception.ResourceNotFoundException;
import com.rag.chatbot.model.ChatSession;
import com.rag.chatbot.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatSessionService {

    private final ChatSessionRepository repo;

    public ChatSession create(CreateSessionRequest request) {
        ChatSession session = new ChatSession();
        session.setUserId(request.userId());
        session.setName(request.name());
        session.setFavorite(request.favorite());
        return repo.save(session);
    }

    public ChatSession get(UUID id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Chat session not found"));
    }

    public void delete(UUID id) {
        repo.deleteById(id);
    }

    @Transactional
    public ChatSession renameSession(UUID sessionId, String name) {
        ChatSession session = repo.findById(sessionId).get();
        session.setName(name);
        return session;
    }
}

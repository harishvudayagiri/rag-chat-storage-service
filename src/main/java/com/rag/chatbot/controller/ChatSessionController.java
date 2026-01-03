package com.rag.chatbot.controller;

import com.rag.chatbot.dto.CreateSessionRequest;
import com.rag.chatbot.dto.RenameSessionRequest;
import com.rag.chatbot.model.ChatSession;
import com.rag.chatbot.service.ChatSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    @PostMapping
    public ResponseEntity<ChatSession> addSession(@RequestBody CreateSessionRequest request) {
        return ResponseEntity.ok(chatSessionService.create(request));
    }

    @PutMapping("/{sessionId}/rename")
    public ResponseEntity<ChatSession> renameSession(@PathVariable UUID sessionId, @RequestBody RenameSessionRequest request) {
        return ResponseEntity.ok(chatSessionService.renameSession(sessionId, request.name()));
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID sessionId) {
        chatSessionService.delete(sessionId);
        return ResponseEntity.noContent().build();
    }

}

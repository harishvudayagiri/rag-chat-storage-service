package com.rag.chatbot.controller;

import com.rag.chatbot.dto.CreateMessageRequest;
import com.rag.chatbot.model.ChatMessage;
import com.rag.chatbot.service.ChatMessageService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/sessions/{sessionId}/messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping()
    public ResponseEntity<ChatMessage> addMessage(@PathVariable UUID sessionId,@RequestBody CreateMessageRequest request) {
        return ResponseEntity.ok().body(chatMessageService.addMessage(sessionId, request));
    }

    @GetMapping
    public Page<ChatMessage> getMessages(@PathVariable UUID sessionId, Pageable pageable) {
        return chatMessageService.getMessages(sessionId, pageable);
    }
}

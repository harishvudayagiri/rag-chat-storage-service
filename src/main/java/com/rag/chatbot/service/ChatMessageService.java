package com.rag.chatbot.service;

import com.rag.chatbot.dto.CreateMessageRequest;
import com.rag.chatbot.model.ChatMessage;
import com.rag.chatbot.model.ChatSession;
import com.rag.chatbot.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionService chatSessionService;

    public ChatMessage addMessage(UUID sessionId, CreateMessageRequest request) {
        ChatSession session = chatSessionService.get(sessionId);

        ChatMessage message = new ChatMessage();
        message.setChatSession(session);
        message.setSender(request.sender());
        message.setContent(request.content());
        message.setRetrievedContext(request.retrievedContext());
        return chatMessageRepository.save(message);
    }

    public Page<ChatMessage> getMessages(UUID sessionId, Pageable pageable) {
        return chatMessageRepository.findByChatSession_Id(sessionId, pageable);
    }
}
